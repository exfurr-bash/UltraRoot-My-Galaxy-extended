plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "dev.busung.s25uroot"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.busung.s25uroot"
        minSdk = 33
        targetSdk = 36
        versionCode = 17
        versionName = "0.2.69"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += "arm64-v8a"
        }

        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=none"
            }
        }
    }

    // Pin NDK so -Werror native builds don't break on toolchain upgrades.
    ndkVersion = "28.2.13676358"

    buildFeatures {
        compose = true
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    signingConfigs {
        // Signing credentials never live in the repo. Local dev can keep a
        // release.keystore next to the project root; CI restores it from the
        // KEYSTORE_BASE64 secret and passes passwords via env vars.
        // Only create the config when env credentials are present; otherwise
        // Gradle would build a config with empty passwords and fail obscurely.
        val keystorePath = System.getenv("KEYSTORE_PATH") ?: "../release.keystore"
        val hasEnvCreds = !System.getenv("KEYSTORE_PASSWORD").isNullOrEmpty() &&
            !System.getenv("KEY_PASSWORD").isNullOrEmpty()
        if (hasEnvCreds || file(keystorePath).isFile) {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("KEY_ALIAS") ?: "release"
                keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        release {
            // Unsigned when no keystore is present (CI signs the APK later
            // with apksigner); signed locally when release.keystore exists.
            val releaseConfig = signingConfigs.findByName("release")
            val releaseKeystore = releaseConfig?.storeFile
            if (releaseConfig != null && releaseKeystore?.isFile == true) {
                signingConfig = releaseConfig
            }
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        // Exact payload profiles bind execution to the bundled root-helper hash.
        // AGP normally strips packaged JNI ELF files in release builds, which changes
        // the helper bytes after the workflow has verified them. Keep it byte-identical
        // to the production payload feed.
        jniLibs.keepDebugSymbols += "**/libcve43499root.so"
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
        )
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.05.01"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.5.0-alpha24")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.materialkolor:material-kolor:4.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("com.airbnb.android:lottie-compose:6.5.0")
    implementation("dev.chrisbanes.haze:haze:1.5.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("org.bouncycastle:bcprov-jdk18on:1.80")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.80")
    implementation("org.bouncycastle:bctls-jdk18on:1.80")

    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20250517")
    androidTestImplementation("androidx.test:core-ktx:1.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:runner:1.7.0")
}
