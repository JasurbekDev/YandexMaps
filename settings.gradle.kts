import java.net.URI

include(":domain:core-api")


include(":data:core-imp")


include(":core")


include(":common:utility-module")


include(":common:ui-module")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://maven.google.com/")
        }
        maven {
            url = URI("https://jitpack.io")
        }
    }
}

rootProject.name = "YandexMaps"
include(":app")
 