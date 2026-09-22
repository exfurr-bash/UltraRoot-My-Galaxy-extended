package dev.busung.s25uroot

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import java.net.NetworkInterface

/**
 * Discovers the wireless-debugging pairing/connect service via mDNS.
 * Discovers wireless-debugging mDNS services.
 */
class AdbMdns(
    context: Context,
    private val serviceType: String,
    private val onPort: (Int) -> Unit,
) {
    private var registered = false
    private var running = false
    private var serviceName: String? = null
    private val nsdManager: NsdManager = context.getSystemService(NsdManager::class.java)

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(serviceType: String) {
            registered = true
        }
        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "onStartDiscoveryFailed: $errorCode")
        }
        override fun onDiscoveryStopped(serviceType: String) {
            registered = false
        }
        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {}
        override fun onServiceFound(serviceInfo: NsdServiceInfo) {
            // NsdManager rejects a ResolveListener that is already in use, and
            // multiple services (or the same service re-announced) can arrive
            // before a prior resolve completes. Create a fresh listener per
            // resolve call so a second onServiceFound never crashes the app.
            nsdManager.resolveService(serviceInfo, createResolveListener())
        }
        override fun onServiceLost(serviceInfo: NsdServiceInfo) {
            if (serviceInfo.serviceName == serviceName) onPort(-1)
        }
    }

    private fun createResolveListener() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}
        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            if (running && isLocalAddress(serviceInfo) && isPortAvailable(serviceInfo.port)) {
                serviceName = serviceInfo.serviceName
                onPort(serviceInfo.port)
            }
        }
    }

    fun start() {
        if (running) return
        running = true
        if (!registered) {
            nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }
    }

    fun stop() {
        if (!running) return
        running = false
        if (registered) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener)
            } catch (_: Exception) {}
        }
    }

    private fun isLocalAddress(info: NsdServiceInfo): Boolean = try {
        val host = info.host ?: return false
        // Accept loopback explicitly (emulator/adbd on 127.0.0.1) plus any
        // address owned by a local interface; compare raw bytes to tolerate
        // IPv6 zone-id / string formatting differences.
        if (host.isLoopbackAddress) return true
        val target = host.address
        NetworkInterface.getNetworkInterfaces().asSequence().any { ni ->
            ni.inetAddresses.asSequence().any { local ->
                local.address.contentEquals(target)
            }
        }
    } catch (_: Exception) {
        false
    }

    private fun isPortAvailable(port: Int): Boolean {
        // Port comes from adbd's own mDNS announcement: validity is range +
        // reachability, not a bind-probe (bind-then-connect races with adbd
        // restarts). The connect step in WirelessAdbSession.open is authoritative.
        if (port <= 0 || port > 65535) return false
        return true
    }

    companion object {
        const val TLS_CONNECT = "_adb-tls-connect._tcp"
        const val TLS_PAIRING = "_adb-tls-pairing._tcp"
        private const val TAG = "AdbMdns"
    }
}
