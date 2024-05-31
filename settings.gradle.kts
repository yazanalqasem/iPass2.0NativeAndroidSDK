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
        jcenter()

    }
}

rootProject.name = "IPassPlus"
include(":app")
include(":IPassPlusSdk")
//include(":document_reader_sdk")
