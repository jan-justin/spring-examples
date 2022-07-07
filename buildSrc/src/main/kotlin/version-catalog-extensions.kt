package com.examples.gradle

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider

val Provider<MinimalExternalModuleDependency>.coordinates: String
    get() = this.get().run { "$module:$versionConstraint" }
