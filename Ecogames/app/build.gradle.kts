plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.semaforo"

    // ✔ API estable compatible con todas tus librerías y Android 15
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.semaforo"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packagingOptions {
        jniLibs.useLegacyPackaging = true
    }
}

dependencies {
    // ✔ VERSIONES ESTABLES (compatibles con compileSdk 34)
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.activity:activity-ktx:1.9.2")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.gridlayout:gridlayout:1.1.0")

    // Navegación estable compatible con API 34
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
