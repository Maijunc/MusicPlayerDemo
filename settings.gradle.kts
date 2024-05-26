pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { //Gradle 构建脚本中用于定义依赖库的位置的部分
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "MusicPlayerDemo"
include(":app")
include(":lib:extensions")
include(":lib:icons_pack")
include(":lib:mediaplayer")
