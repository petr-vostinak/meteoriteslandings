import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "cz.vostinak.meteoritelandings"
    compileSdk = 34

    defaultConfig {
        applicationId = "cz.vostinak.meteoritelandings"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures { buildConfig = true }

    //load the values from .properties file
    val keystoreFile = project.rootProject.file("local.properties")
    val properties = Properties()
    properties.load(keystoreFile.inputStream())
    //return empty key in case something goes wrong
    val mapApiKey = properties.getProperty("MAPS_API_KEY") ?: ""
    val apiAppToken = properties.getProperty("API_APP_TOKEN") ?: ""

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField(
                type = "String",
                name = "BUILD_TYPE",
                value = "\"Debug\""
            )
            buildConfigField(
                type = "String",
                name = "API_APP_TOKEN",
                value = "\"$apiAppToken\""
            )
            buildConfigField(
                type = "String",
                name = "MAPS_API_KEY",
                value = "\"$mapApiKey\""
            )
            manifestPlaceholders["MAPS_API_KEY"] = mapApiKey
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                type = "String",
                name = "BUILD_TYPE",
                value = "\"Release\""
            )
            buildConfigField(
                type = "String",
                name = "API_APP_TOKEN",
                value = "$apiAppToken\""
            )
            buildConfigField(
                type = "String",
                name = "MAPS_API_KEY",
                value = "\"$mapApiKey\""
            )
            manifestPlaceholders["MAPS_API_KEY"] = mapApiKey
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    androidResources {
        generateLocaleConfig = true
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

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Gson
    implementation(libs.gson)

    // Jackson
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)

    // Apache commons
    implementation(libs.commons.lang3)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Coil
    implementation(libs.coil)

    // compose foundation (LazyColumn etc.)
    implementation(libs.androidx.compose.foundation)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.material)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // location
    implementation(libs.play.services.location)

    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}