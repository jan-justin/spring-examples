import com.examples.gradle.coordinates

plugins {
    `example-kotlin-jvm`
    `example-kotlin-spring-boot`
}

dependencies {
    implementation(libs.bundles.kotlin.reactive)
    implementation(libs.jackson.kotlin)
    implementation(libs.spring.boot.oauth2.resource.server)
    implementation(libs.spring.boot.webflux)
    testImplementation(testLibs.bundles.testcontainers)
    testImplementation(testLibs.keycloak.testcontainers)
}

dependencyManagement {
    imports {
        mavenBom(testLibs.testcontainers.bom.coordinates)
    }
}
