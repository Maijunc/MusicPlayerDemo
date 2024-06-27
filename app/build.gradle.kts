plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")

}

android {
    namespace = "com.androidcourse.musicplayerdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.androidcourse.musicplayerdemo"
        minSdk = 30
        targetSdk = 34
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

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.cardview)

//    implementation(libs.multi.sliding.up.panel)
//    implementation(libs.readable.bottom.bar)

    implementation("com.github.realgearinc:multi-sliding-up-panel:1.3.0")
    implementation("com.github.realgearinc:readable-bottom-bar:1.0.6")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.material)

    implementation(project(path = ":lib:extensions"))
    implementation(project(path = ":lib:icons_pack"))
    implementation(project(path = ":lib:mediaplayer"))

    //  生命周期感知型组件可执行操作来响应另一个组件（如 activity 和 fragment）的生命周期状态的变化。这些组件有助于您写出更有条理且往往更精简的代码，这样的代码更易于维护。
    val lifecycle_version = "2.8.0"
    //  Room 持久性库在 SQLite 的基础上提供了一个抽象层，让用户能够在充分利用 SQLite 的强大功能的同时，获享更强健的数据库访问机制。
    val room_version = "2.6.1"
    val glide_version = "4.15.1"

    // ViewModel
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
//    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

//    implementation(libs.androidx.room.runtime)
//    annotationProcessor(libs.room.compiler)
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

//    implementation(libs.glide)
    // Skip this if you don't want to use integration libraries or configure Glide.
//    annotationProcessor(libs.compiler)

    implementation("com.github.bumptech.glide:glide:$glide_version")
//    annotationProcessor("com.github.bumptech.glide:compiler:$glide_version")
    kapt("com.github.bumptech.glide:compiler:4.14.2")


}
