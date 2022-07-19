package com.examples.spring

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class CustomAutoconfigurationTests {
    companion object {
        val applicationContext: ApplicationContextRunner = ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CustomAutoconfiguration::class.java))
    }

    @Test
    fun `should provide jwt converter bean`() {
        applicationContext.run {
            assertThat(it).hasSingleBean(JwtAuthenticationConverter::class.java)
        }
    }
}