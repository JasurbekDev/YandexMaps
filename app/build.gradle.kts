import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ksp)
}

val mapkitApiKey: String by extra {
    loadMapkitApiKey()
}

fun loadMapkitApiKey(): String {
    val properties = Properties()
    project.rootProject.file("./local.properties").inputStream().use { properties.load(it) }
    return properties.getProperty("MAPKIT_API_KEY", "")
}

android {
    namespace = "com.idyllic.yandexmaps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.idyllic.yandexmaps"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "MyApp-${versionName}")

        buildConfigField("String", "MAPKIT_API_KEY", "\"${mapkitApiKey}\"")
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
        viewBinding = true
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
    implementation(libs.constraintlayout)
    implementation(project(":common:ui-module"))
    implementation(project(":core"))
    implementation(project(":common:utility-module"))
    implementation(project(":domain:core-api"))
    implementation(project(":data:core-imp"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.work.runtime.ktx)

    // Common
    implementation(libs.bundles.common.ui.implementation)
    ksp(libs.bundles.common.ui.ksp)
    kspTest(libs.bundles.common.ui.ksp.test)
    kspAndroidTest(libs.bundles.common.ui.ksp.android.test)
    androidTestImplementation(libs.bundles.common.ui.android.test.implementation)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)

    // Yandex.Maps
    implementation(libs.yandex.maps)
}