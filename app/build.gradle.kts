import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.daggerHiltPlugin)
}

android {
    namespace = "com.rodcollab.mymarvelcomics"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rodcollab.mymarvelcomics"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        android.buildFeatures.buildConfig = true
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())
        buildConfigField("String", "PUBLIC_API_KEY", properties.getProperty("PUBLIC_API_KEY"))
        buildConfigField("String", "PRIVATE_API_KEY", properties.getProperty("PRIVATE_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
}

dependencies {

    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.roboeletric)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.test.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.material)

    testImplementation(libs.okhttp.interceptor)
    testImplementation(libs.okhttp)
    implementation(libs.coil.compose)

    implementation(libs.dagger.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.navigation.compose)
    implementation(libs.navigation.compose.hilt)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

}