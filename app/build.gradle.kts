plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.helloworldapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.helloworldapp"
        minSdk = 29
        targetSdk = 36
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
    // Firebase - Ensure you have firebase.bom defined in your libs.versions.toml
    // Example for libs.versions.toml:
    // [versions]
    // firebaseBom = "33.1.2"
    // [libraries]
    // firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
    // firebase-auth = { group = "com.google.firebase", name = "firebase-auth-ktx" } // No version needed here if using BOM

    implementation(platform(libs.firebase.bom)) // <-- ADD OR ENSURE THIS LINE IS PRESENT
    implementation(libs.firebase.auth)

    // Correct way to add navigation-compose using your version catalog
    implementation(libs.androidx.navigation.compose)

    // Other dependencies (cleaned up)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7") // Keep as is, it's for testing

    // For Compose UI tests, these versions will be managed by the Compose BOM
    // androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.0") // Will be managed by BOM
    // debugImplementation ("androidx.compose.ui:ui-test-manifest:1.5.0") // Will be managed by BOM

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // This is important!
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.iconsExtended) // <-- ADDED THIS LINE

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Ensure test BOM is also present
    androidTestImplementation(libs.androidx.ui.test.junit4) // Version managed by BOM
    debugImplementation(libs.androidx.ui.tooling)          // Version managed by BOM
    debugImplementation(libs.androidx.ui.test.manifest)    // Version managed by BOM
}
