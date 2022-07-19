import com.examples.gradle.coordinates

plugins {
    `example-kotlin-jvm`
    `example-kotlin-spring-boot`
}

dependencies {
    implementation(libs.jackson.kotlin)
    implementation(libs.spring.boot.oauth2.resource.server)
    testImplementation(libs.spring.boot.web)
    testImplementation(testLibs.bundles.testcontainers)
    testImplementation(testLibs.keycloak.testcontainers)
}

dependencyManagement {
    imports {
        mavenBom(testLibs.testcontainers.bom.coordinates)
    }
}

tasks.bootJar {
    enabled = false
}
