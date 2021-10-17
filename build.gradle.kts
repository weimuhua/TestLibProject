// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version = "1.4.31"
    extra["kotlin_version"] = kotlin_version
    extra["kotlin_coroutines"] = "1.3.3"
    extra["DEBUG_LOG"] = "true"
    extra["SUPPORT_V7_VER"] = "true"
    extra["compileSdkVersion"] = 30
    extra["minSdkVersion"] = 21
    extra["targetSdkVersion"] = 22
    extra["sourceCompatibility"] = JavaVersion.VERSION_1_8
    extra["targetCompatibility"] = JavaVersion.VERSION_1_8

    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven(uri("./gradle_repo"))
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.tencent.now:flutterplugin:1.0.0")
        classpath(kotlin("gradle-plugin", version = "$kotlin_version"))
    }
}

allprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven(uri("./gradle_repo"))
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
