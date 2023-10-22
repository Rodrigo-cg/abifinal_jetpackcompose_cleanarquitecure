import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id ("com.google.gms.google-services")
    id( "kotlin-kapt")
    id("kotlin-parcelize")

}

android {
    namespace = "com.abi.abifinal"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.abi.abifinal"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            exclude("META-INF/LICENSE.md")
            exclude("META-INF/NOTICE.md")
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // Import the BoM for the Firebase platform
    implementation( platform("com.google.firebase:firebase-bom:32.2.2"))

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("org.chromium.net:cronet-embedded:113.5672.61")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("com.google.android.gms:play-services-location:21.0.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    val nav_version = "2.5.3"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui:1.5.0-alpha04")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")





    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    // Allow references to generated code
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    //GSON
    implementation ("com.google.code.gson:gson:2.9.0")
    //BLE
    implementation ("com.github.LeandroSQ:android-ble-made-easy:1.8.2")
    //ASYNC image
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation ("commons-io:commons-io:2.7")
    //Permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.21.1-beta")
    // Location Services
    implementation ("com.google.android.gms:play-services-location")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}