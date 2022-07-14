package com.examples.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class ApplicationConfiguration {
    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain =
        http {
            authorizeRequests {
                authorize(HttpMethod.GET, "/api/todos", hasAuthority("reader"))
                authorize(HttpMethod.POST, "/api/todos", hasAuthority("writer"))
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = grantedAuthoritiesExtractor()
                }
            }
        }.run {
            http.build()
        }

    fun grantedAuthoritiesExtractor(): JwtAuthenticationConverter =
        JwtAuthenticationConverter().apply { setJwtGrantedAuthoritiesConverter(keycloakGrantedAuthoritiesConverter()) }

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

@RestController
@RequestMapping("/api/todos")
class TodoController {
    private val todos = mutableListOf<String>()

    @GetMapping
    fun readTodos(): List<String> =
        todos

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@RequestBody todo: String) {
        todos += todo
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
