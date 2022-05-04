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
- Advanced Search, passing an expression from front, such as following JSON -> `SearchSpecification`

```json
{
  "startIndex": 1,
  "count": 4,
  "filter": {
    "operator": "And",
    "operands": [
      {
        "operator": "GreaterThan",
        "property": "price",
        "value": "50"
      },
      {
        "operator": "GreaterThanEqual",
        "property": "restaurant.rate",
        "value": "3"
      }
    ]
  },
  "sorts": [
    {
      "property": "name"
    }
  ]
}
```

## Quick Run

Run project using `test-jwt` profile for using JWT as security token.

```shell
mvn clean compile spring-boot:start -Dspring-boot.run.profiles=test-jwt
mvn artemis:run
mvn spring-boot:stop
```

Now, run project using `test-cookie` profile for using cookie as security token.

```shell
mvn clean compile spring-boot:start -Dspring-boot.run.profiles=test-cookie
mvn artemis:run -DxmlName=artemis-cookie -DgroovyName=artemis
mvn spring-boot:stop
```