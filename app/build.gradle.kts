plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.abc.see_sky"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.abc.see_sky"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // AndroidX Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Image Loading Library
    implementation(libs.picasso)

    // Retrofit for API Calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Google Play Services for Location Access
    implementation (libs.play.services.location)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
