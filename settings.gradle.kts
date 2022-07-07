@file:Suppress("UnstableApiUsage")

rootProject.name = "spring-examples"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("./libs.versions.toml"))
        }

        create("testLibs") {
            from(files("./test-libs.versions.toml"))
        }
    }
}

include(
    "boot--web--keycloak-starter",
    "boot--web--security--keycloak-adapter",
)
