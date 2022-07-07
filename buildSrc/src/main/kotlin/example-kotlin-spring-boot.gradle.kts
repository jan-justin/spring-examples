import org.gradle.api.artifacts.VersionCatalog
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

pluginManager.withPlugin("java") {
    val catalogs = project.extensions.getByType<VersionCatalogsExtension>()
    val libs = catalogs.named("libs")
    val testLibs = catalogs.named("testLibs")
    with(dependencies) {
        addProvider("implementation", libs.findBundle("kotlin").get())
        addProvider("testImplementation", testLibs.findBundle("spring-boot").get())
    }
}
