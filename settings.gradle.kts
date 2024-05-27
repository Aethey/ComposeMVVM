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
    repositories {
        google()
        mavenCentral()
        //maven(url = "https://storage.googleapis.com/download.flutter.io")
        //maven(url = "git_simple_flutter_moudle/build/host/outputs/repo")
    }

}

rootProject.name = "GitSimpleDemo"
include(":app")


