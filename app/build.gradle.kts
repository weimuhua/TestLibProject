import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}
apply(plugin = "com.tencent.now.flutterplugin")

android {
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
    defaultConfig {
        applicationId = "baidu.com.testlibproject"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 1
        versionName = "1.0"
        buildConfigField("boolean", "DEBUG_LOG", rootProject.extra["DEBUG_LOG"].toString())

        packagingOptions {
            resources.excludes.add("META-INF/proguard/coroutines.pro")
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        register("release") {
            storeFile = file("my-release-key.keystore")
            storePassword = "android"
            keyAlias = "alias_name"
            keyPassword = "android"
        }
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))

            signingConfig = signingConfigs.getByName("release")
        }
        named("debug") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":CommonTools"))
    implementation(project(":annotation"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")

    annotationProcessor(project(":processor"))
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("com.android.support:appcompat-v7:" + rootProject.extra["SUPPORT_V7_VER"])
    implementation("androidx.annotation:annotation:1.2.0")
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["kotlin_coroutines"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["kotlin_coroutines"]}")

    // Required -- JUnit 4 framework
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:3.5.11")
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
}
