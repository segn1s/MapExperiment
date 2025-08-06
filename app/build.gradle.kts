plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.segn1s.mapexperiment"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.segn1s.mapexperiment"
        minSdk = 29
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
        buildConfig = true
    }
    buildTypes.configureEach {
        buildConfigField("String", "TOMTOM_API_KEY", "\"Tj8J2oy0l2WFErnYeE9Ctua4ZsmnFAxW\"")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx) // Основные расширения AndroidX
    implementation(libs.androidx.appcompat) // Поддержка обратной совместимости
    implementation(libs.material) // Материальные компоненты дизайна
    implementation(libs.androidx.activity) // Компоненты Activity
    implementation(libs.androidx.constraintlayout) // ConstraintLayout для разметки
    testImplementation(libs.junit) // JUnit для модульных тестов
    androidTestImplementation(libs.androidx.junit) // JUnit для инструментальных тестов
    androidTestImplementation(libs.androidx.espresso.core) // Espresso для UI-тестов
    val tomtomVersion = "1.25.3"
    implementation("com.tomtom.sdk.maps:map-display:$tomtomVersion") // TomTom SDK для отображения карты
}