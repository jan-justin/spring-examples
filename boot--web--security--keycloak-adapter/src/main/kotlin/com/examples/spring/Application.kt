package com.examples.spring

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

@KeycloakConfiguration
class SecurityConfiguration : KeycloakWebSecurityConfigurerAdapter() {
    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(keycloakAuthenticationProvider())
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy =
        RegisterSessionAuthenticationStrategy(buildSessionRegistry())

    @Bean
    protected fun buildSessionRegistry(): SessionRegistry =
        SessionRegistryImpl()

    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/todos*").hasAuthority("reader")
            .antMatchers(HttpMethod.POST, "/api/todos*").hasAuthority("writer")
            .and()
            .csrf().ignoringAntMatchers("/api/*")
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
