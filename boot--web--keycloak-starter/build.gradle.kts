@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.keycloak.boot)
    implementation(libs.jackson.kotlin)
    implementation(libs.spring.boot.web)
    testImplementation(testLibs.bundles.spring.boot)
    testImplementation(testLibs.bundles.testcontainers)
    testImplementation(testLibs.keycloak.testcontainers)
}

val MinimalExternalModuleDependency.coordinates: String
    get() = "$module:$versionConstraint"

dependencyManagement {
    imports {
        mavenBom(libs.keycloak.bom.get().coordinates)
        mavenBom(testLibs.testcontainers.bom.get().coordinates)
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = java.sourceCompatibility.toString()
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
