package com.examples.spring

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
    @Autowired
    private lateinit var test: WebTestClient

    @Test
    fun `users should require authentication`() {
        test.readTodos()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `users with 'reader' role should be permitted to read todo items`() {
        test.readTodosUsing(accessTokenWithoutWriterRole())
            .expectStatus().isOk
    }

    @Test
    fun `users should require 'writer' role to create todo item`() {
        test.createTodoUsing(accessTokenWithoutWriterRole())
            .expectStatus().isForbidden
    }

    @Test
    fun `users with 'writer' role should be permitted to create todo item`() {
        test.createTodoUsing(accessTokenWithWriterRole())
            .expectStatus().isCreated
    }

    companion object {
        @JvmStatic
        @Container
        val keycloak: KeycloakContainer =
            KeycloakContainer().withRealmImportFile("/todos-realm.json")

        private val realm by lazy {
            "${keycloak.authServerUrl}realms/todos"
        }

        private val oidc by lazy {
            WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .baseUrl("$realm/protocol/openid-connect")
                .build()
        }

        private fun accessTokenUsing(clientId: String): String =
            oidc.post()
                .uri("/token")
                .body(BodyInserters.fromFormData("grant_type", "password")
                    .with("client_id", clientId)
                    .with("username", "jane")
                    .with("password", "jane")
                    .with("scope", "openid")
                )
                .retrieve()
                .toEntity<Map<String, Any>>()
                .block()!!
                .body!!["access_token"] as String

        fun accessTokenWithWriterRole(): String =
            accessTokenUsing("todos-public-client-with-writer-role")

        fun accessTokenWithoutWriterRole(): String =
            accessTokenUsing("todos-public-client-without-writer-role")

        fun WebTestClient.readTodos(): ResponseSpec =
            this.readTodosUsing(null)

        fun WebTestClient.readTodosUsing(token: String?): ResponseSpec =
            this.get()
                .uri("/api/todos")
                .withAuthorization(token)
                .exchange()

        fun WebTestClient.createTodoUsing(token: String?, todo: String = "Test TODO"): ResponseSpec =
            this.post()
                .uri("/api/todos")
                .withAuthorization(token)
                .bodyValue(todo)
                .exchange()

        @Suppress("Unchecked_Cast")
        private fun <T : RequestHeadersSpec<*>> T.withAuthorization(token: String?): T =
            if (token.isNullOrBlank()) this
            else this.header(HttpHeaders.AUTHORIZATION, "Bearer $token") as T

        @JvmStatic
        @DynamicPropertySource
        fun oauth2Properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri") { realm }
        }
    }
}
