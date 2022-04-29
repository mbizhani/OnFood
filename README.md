# OnFood

A (sample) Online Food by Spring Boot!

This application addresses some technical issues and the solution in Spring Boot:

- REST API test -> Artemis
- Database Migration -> Liquibase
- Security with JWT or Cookie -> `SecurityTokenFilter`, `SecurityService`, and custom `SecurityAuthenticationToken`
- Handle POJO conversion (DTO to entity and vice versa) -> MapStruct & `IBeanMapper`
- Store auditable information for entities -> `Auditable` & `AuditedUser`
- Centralize exception handling for controllers (presentation) -> `@ControllerAdvice`
- Document REST endpoints -> Swagger at [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
- Using one exception class and multiple enumeration for error emission -> `OnFoodException`, `FoodErrorCode`,
  and `RestaurateurErrorCode`
- Advanced Search -> `SearchSpecification`

## Quick Run

```shell
mvn spring-boot:start -Dspring-boot.run.profiles=test-cookie
mvn artemis:run
mvn spring-boot:stop
```