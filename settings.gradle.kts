pluginManagement {
    repositories {
        maven(url = "https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven(url = "./gradle_repo")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
    }
}
dependencyResolutionManagement {
    repositories {
        maven(url = "https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven(url = "./gradle_repo")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
    }
}
rootProject.name = "TestLibProject"
include(":app")
include(":annotation")
include(":processor")
include(":CommonTools")
include(":flutter_transform_plugin")
include(":nativelib")

project(":CommonTools").projectDir = file("submodules/CommonTools")
project(":processor").projectDir = file("submodules/processor")
project(":annotation").projectDir = file("submodules/annotation")
project(":nativelib").projectDir = file("submodules/nativelib")
project(":flutter_transform_plugin").projectDir = file("submodules/flutter_transform_plugin")

