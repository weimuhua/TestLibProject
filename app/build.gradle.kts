import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}
apply(plugin = "com.tencent.now.flutterplugin")

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)

    defaultConfig {
        applicationId = "baidu.com.testlibproject"
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["targetSdkVersion"] as Int)
        versionCode = 1
        versionName = "1.0"
        buildConfigField("boolean", "DEBUG_LOG", rootProject.extra["DEBUG_LOG"].toString())

        packagingOptions {
            exclude("META-INF/proguard/coroutines.pro")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":CommonTools"))
    implementation(project(":annotation"))
    annotationProcessor(project(":processor"))
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.android.support:appcompat-v7:" + rootProject.extra["SUPPORT_V7_VER"])
    implementation("androidx.annotation:annotation:1.0.0")
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["kotlin_coroutines"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["kotlin_coroutines"]}")

    // Required -- JUnit 4 framework
    testImplementation("junit:junit:4.13.1")
    testImplementation("androidx.test:core:1.3.0")
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:3.5.11")
    testImplementation("org.robolectric:robolectric:4.5-alpha-3")
    testImplementation("androidx.test.ext:junit:1.1.2")
}
