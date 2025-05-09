plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    alias (libs.plugins.dagger.hilt)
}

android {
    namespace = "com.example.connect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.connect"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Icons
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics.ktx)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // EncryptedSharedPreferences & Crypto
    implementation(libs.androidx.security.crypto)
    implementation(libs.compressor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Biometrics
    implementation(libs.biometric)

    // Google Play Services Auth
    implementation(libs.play.services.auth)

    // ImagePickerHelper
    implementation(libs.imagepicker)

    // Material3 (explicit version)
    implementation("androidx.compose.material3:material3:1.3.1")

    // Coil Compose
    implementation(libs.coil.compose)
    implementation(libs.coil.video)

    // Retrofit & Moshi
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Compose
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.animation)

    // ExoPlayer
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.ui)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Google Play Services Location
    implementation(libs.play.services.location)

    // Google Maps Compose
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widgets)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // ZXing for generation & scanning
    implementation (libs.zxing.android.embedded)
    implementation (libs.core)

    // For IntentIntegrator
    implementation (libs.androidx.fragment.ktx)
}
