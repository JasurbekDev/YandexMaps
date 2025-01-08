@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.idyllic.core_imp"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

//        buildConfigField("String", "BASE_URL_TEST", "\"https://dummyjson.com/\"")
//        buildConfigField("String", "BASE_URL_PROD_API", "\"https://dummyjson.com/\"")

        buildConfigField("String", "BASE_URL_TEST", "\"http://api.manzill.uz/v1/\"")
        buildConfigField("String", "BASE_URL_PROD_API", "\"http://api.manzill.uz/v1/\"")
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(":domain:core-api"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Common
    implementation(libs.bundles.common.ui.implementation)
    ksp(libs.bundles.common.ui.ksp)
    kspTest(libs.bundles.common.ui.ksp.test)
    kspAndroidTest(libs.bundles.common.ui.ksp.android.test)
    androidTestImplementation(libs.bundles.common.ui.android.test.implementation)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.gson)

    // Chucker
    debugApi(libs.chucker)
    releaseApi(libs.chucker)

    implementation(libs.androidx.security.crypto)
}