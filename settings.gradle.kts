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
    "boot--custom-autoconfiguration",
    "boot--web--keycloak-starter",
    "boot--web--oauth2-resource-server",
    "boot--web--security--keycloak-adapter",
    "boot--web--security--keycloak-adapter-and-starter",
    "boot--webflux--oauth2-resource-server",
)
