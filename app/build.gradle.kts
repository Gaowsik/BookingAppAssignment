import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.dagger.hilt.android.get().pluginId)
    id(libs.plugins.google.service.get().pluginId)
    id(libs.plugins.safeargs.get().pluginId)
    alias(libs.plugins.google.map.secret) apply false
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {

    namespace = "com.example.bookingappassignment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.bookingappassignment"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY")
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
        viewBinding = true
        dataBinding = true
    }
    buildFeatures {
        buildConfig = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.dagger.hild.android)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    kapt(libs.dagger.hild.android.compiler)
    kapt(libs.persistence.room.compiler)

    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.convert.gson)
    implementation(libs.retrofit2.convert.scalars)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.messaging)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.singledateandtimepicker)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}