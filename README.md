# OnFood

A (sample) Online Food by Spring Boot!

This application addresses some technical issues and the solution in Spring Boot:

- REST API test -> Artemis
- Database Migration -> Liquibase
- Security with JWT -> `JwtFilter`, `SecurityService`, and custom `AuthenticationToken`
- Handle POJO conversion (DTO to entity and vice versa) -> MapStruct & `IBeanMapper`
- Store auditable information for entities -> `Auditable` & `AuditedUser`
- Centralize exception handling for controllers (presentation) -> `@ControllerAdvice`
- Document REST endpoints -> Swagger
- Using one exception class and multiple enumeration for error emission -> `OnFoodException`, `FoodErrorCode`,
  and `RestaurateurErrorCode`
- Advanced Search -> ?