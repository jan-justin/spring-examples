# Keycloak Spring Security Adapter with Keycloak Spring Boot Starter Example

This project demonstrates the configuration of a Spring Boot Web MVC RESTful resource server with
[Keycloak](https://www.keycloak.org/) as an Identity and Access Management provider.

Note that this particular example makes use of the Keycloak
[Spring Security Adapter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_security_adapter) 
as well as the Keycloak
[Spring Boot Starter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_boot_adapter). Do bear
in mind the additional considerations when combining both, as is mentioned 
[here](https://www.keycloak.org/docs/latest/securing_apps/#spring-boot-integration).

The Keycloak Spring Security Adapter is a dependency of the Keycloak Spring Boot Starter, and as such one need only
specify the Keycloak Spring Boot Starter as a dependency for one's own project.
