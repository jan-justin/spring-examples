import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            val targetCompatibility: String by project
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = targetCompatibility
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
