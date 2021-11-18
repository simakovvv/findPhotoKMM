plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.electrolux.findphoto.android"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
val composeVersion = findProperty("version.compose") as String

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")

    implementation("androidx.compose.foundation:foundation:$composeVersion")
    // Compose Material Design
    implementation("androidx.compose.material:material:$composeVersion")
    // When using a MDC theme
    implementation ("com.google.android.material:compose-theme-adapter:1.1.0")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")

    //Compose utils
    implementation("com.google.accompanist:accompanist-insets:0.20.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")

    // Integration with activities
    implementation ("androidx.activity:activity-compose:1.4.0")

    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")

    // Nvigation between fragments
    implementation (  "androidx.navigation:navigation-compose:2.4.0-beta02")

    // UI Tests
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.0.5")

    // Images downloading
    implementation("io.coil-kt:coil-compose:1.4.0")
}