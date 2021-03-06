import com.examples.gradle.coordinates

plugins {
    `example-kotlin-jvm`
    `example-kotlin-spring-boot`
}

dependencies {
    implementation(libs.keycloak.boot)
    implementation(libs.jackson.kotlin)
    implementation(libs.spring.boot.web)
    testImplementation(testLibs.bundles.testcontainers)
    testImplementation(testLibs.keycloak.testcontainers)
}

dependencyManagement {
    imports {
        mavenBom(libs.keycloak.bom.coordinates)
        mavenBom(testLibs.testcontainers.bom.coordinates)
    }
}
