package com.examples.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.net.URI

@SpringBootApplication
class Application

@EnableWebFluxSecurity
@Configuration(proxyBeanMethods = false)
class ApplicationConfiguration {
    @Bean
    fun todoHandler(): TodoHandler =
        TodoHandler()

    @Bean
    fun router(todos: TodoHandler): RouterFunction<ServerResponse> =
        coRouter {
             "/api".nest {
                 accept(MediaType.APPLICATION_JSON).nest {
                     GET("/todos", todos::getAll)
                     POST("/todos", todos::create)
                 }
            }
        }

    @Bean
    fun httpSecurity(http: ServerHttpSecurity): SecurityWebFilterChain =
        http {
            authorizeExchange {
                authorize(pathMatchers(HttpMethod.GET, "/api/todos"), hasAuthority("reader"))
                authorize(pathMatchers(HttpMethod.POST, "/api/todos"), hasAuthority("writer"))
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = grantedAuthoritiesExtractor()
                }
            }
        }

    fun grantedAuthoritiesExtractor(): Converter<Jwt, Mono<AbstractAuthenticationToken>> =
        JwtAuthenticationConverter().apply { setJwtGrantedAuthoritiesConverter(keycloakGrantedAuthoritiesConverter()) }
            .let(::ReactiveJwtAuthenticationConverterAdapter)

    fun keycloakGrantedAuthoritiesConverter(): Converter<Jwt, Collection<GrantedAuthority>> =
        Converter { jwt ->
            val authorities = jwt.claims["resource_access"]
                ?.let { (it as Map<*, *>)["todos-rest-api"] }
                ?.let { (it as Map<*, *>)["roles"] }
                ?.let { it as List<*> }
                ?: emptyList<Any>()
            authorities.map(Any?::toString)
                .map(::SimpleGrantedAuthority)
        }
}

class TodoHandler {
    private val todos = mutableListOf<String>()

    suspend fun getAll(request: ServerRequest): ServerResponse =
        ServerResponse.ok()
            .bodyValueAndAwait(todos)

    suspend fun create(request: ServerRequest): ServerResponse =
        ServerResponse.created(URI("http://example.com/api/todos"))
            .buildAndAwait()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
