plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.modaurbana"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.modaurbana"
        minSdk = 24
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
    // Core Android / Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ✅ Compose BOM (solo uno, moderno)
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // ✅ Módulos de Compose usando el BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Tests básicos Compose
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ViewModel / Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Navigation Compose (deja solo una versión)
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Activity Compose (ya la tienes via libs, pero si quieres explícita, mantén solo una)
    implementation("androidx.activity:activity-compose:1.9.3")

    // Texto extra de Compose (opcional)
    implementation("androidx.compose.ui:ui-text")

    // OkHttp - Cliente HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Retrofit - Cliente REST
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // DataStore - Para guardar tokens
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coil - Para cargar imágenes desde URLs
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Testing - JUnit
    testImplementation("junit:junit:4.13.2")

    // Testing - Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Testing - Mockk
    testImplementation("io.mockk:mockk:1.13.8")                  // unit tests
    androidTestImplementation("io.mockk:mockk-android:1.13.8")   // androidTest

    // Testing - Turbine
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("app.cash.turbine:turbine:1.0.0")


    // Testing - Core testing
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Android instrumented tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}