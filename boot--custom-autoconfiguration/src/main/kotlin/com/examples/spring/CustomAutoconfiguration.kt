package com.examples.spring

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@AutoConfiguration
class CustomAutoconfiguration {
    @Bean
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
