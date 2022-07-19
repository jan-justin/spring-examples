package com.examples.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class ApplicationConfiguration {
    @Bean
    fun httpSecurity(
        http: HttpSecurity,
        converter: JwtAuthenticationConverter
    ): SecurityFilterChain =
        http {
            authorizeRequests {
                authorize(HttpMethod.GET, "/api/todos", hasAuthority("reader"))
                authorize(HttpMethod.POST, "/api/todos", hasAuthority("writer"))
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = converter
                }
            }
        }.run {
            http.build()
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
