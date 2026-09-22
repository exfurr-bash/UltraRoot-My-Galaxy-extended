package dev.busung.s25uroot.ui.hud

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

/** Central brutal feedback: haptics + synthesized SFX (no binary assets). */

fun brutalHaptic(view: View, kind: BrutalHaptic = BrutalHaptic.Click) {
    when (kind) {
        BrutalHaptic.Click -> view.performHapticFeedback(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
            else HapticFeedbackConstants.LONG_PRESS,
        )
        BrutalHaptic.Heavy -> {
            val vibrator = view.context.systemVibrator()
            vibrator?.vibrateOnce(60, 200)
        }
        BrutalHaptic.Success -> {
            val vibrator = view.context.systemVibrator()
            vibrator?.vibratePattern(longArrayOf(0, 40, 60, 90), intArrayOf(180, 0, 255, 0))
        }
        BrutalHaptic.Fail -> {
            val vibrator = view.context.systemVibrator()
            vibrator?.vibratePattern(longArrayOf(0, 90, 60, 90), intArrayOf(255, 0, 255, 0))
        }
    }
}

enum class BrutalHaptic { Click, Heavy, Success, Fail }

private fun Context.systemVibrator(): Vibrator? = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
}.getOrNull()

private fun Vibrator.vibrateOnce(ms: Long, amplitude: Int) {
    runCatching {
        vibrate(VibrationEffect.createOneShot(ms, amplitude.coerceIn(1, 255)))
    }
}

private fun Vibrator.vibratePattern(timings: LongArray, amplitudes: IntArray) {
    runCatching {
        vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
    }
}

enum class HudSfx { Click, Confirm, Error, RankUp, Phase }

class HudSound(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun play(kind: HudSfx, enabled: Boolean) {
        if (!enabled) return
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        if (audio?.ringerMode != AudioManager.RINGER_MODE_NORMAL) return
        scope.launch {
            runCatching {
                when (kind) {
                    HudSfx.Click -> tone(2200f, 30, 0.25f)
                    HudSfx.Confirm -> { tone(660f, 70, 0.4f); tone(990f, 90, 0.4f) }
                    HudSfx.Error -> tone(140f, 180, 0.5f, square = true)
                    HudSfx.RankUp -> { tone(523f, 60, 0.35f); tone(784f, 60, 0.35f); tone(1046f, 120, 0.4f) }
                    HudSfx.Phase -> tone(440f, 50, 0.3f, square = true)
                }
            }
        }
    }

    private fun tone(freq: Float, ms: Int, volume: Float, square: Boolean = false) {
        val rate = 22050
        val n = (rate * ms / 1000)
        val buffer = ShortArray(n) { i ->
            val t = i.toFloat() / rate
            val s = sin(2 * Math.PI * freq * t).toFloat()
            val shaped = if (square) (if (s > 0) 1f else -1f) * 0.6f + s * 0.4f else s
            // Sharp decay envelope for percussive click.
            val env = (1f - i.toFloat() / n)
            (shaped * env * env * volume * Short.MAX_VALUE).toInt().toShort()
        }
        val track = android.media.AudioTrack(
            AudioManager.STREAM_MUSIC,
            rate,
            android.media.AudioFormat.CHANNEL_OUT_MONO,
            android.media.AudioFormat.ENCODING_PCM_16BIT,
            buffer.size * 2,
            android.media.AudioTrack.MODE_STATIC,
        )
        track.write(buffer, 0, buffer.size)
        track.play()
        Thread.sleep(ms + 20L)
        track.stop()
        track.release()
    }
}

@Composable
fun rememberHudSound(): HudSound {
    val context = LocalContext.current
    val sound = remember(context) { HudSound(context.applicationContext) }
    DisposableEffect(Unit) { onDispose { } }
    return sound
}
