# Keycloak Spring Boot Starter Example

This project demonstrates the configuration of a Spring Boot Webflux RESTful resource server with
[Keycloak](https://www.keycloak.org/) as an Identity and Access Management provider.

This particular example uses Keycloak as an OAuth2 Authorization Server, but does not use any Keycloak-specific
dependencies. Instead, it relies on the more generic configuration options provided by 
[Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/reactive/oauth2/resource-server/index.html).
For convenience, the [Spring Boot Security OAuth2 Resource Server Starter](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.security.oauth2.server) 
is used to autoconfigure JWT bootstrapping and validation.
