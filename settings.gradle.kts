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
    }
}

rootProject.name = "QuickBill"
include(":app")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":feature:counter")
include(":feature:invoice")
include(":feature:catalog")
include(":feature:rolodex")
include(":feature:tickets")
include(":feature:cashdrawer")
include(":feature:corkboard")
