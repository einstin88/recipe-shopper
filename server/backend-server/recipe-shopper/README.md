# Features

1. Endpoints
2. Test endpoints
3. Scheduling
4. Security (authentication + authorization)

### Task Scheduling

[Annotation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html)
[Guide](https://spring.io/guides/gs/scheduling-tasks/)

## Maven with DEV profile settings

`mvn spring-boot:run -D"spring-boot.run.profiles=dev"`

## Bonus Features

### Javadoc

[reference](https://www.oracle.com/sg/technical-resources/articles/java/javadoc-tool.html#format)

### Spring Security

1. [OAuth Server]()
2. [OAuth Client](https://docs.spring.io/spring-authorization-server/docs/current/reference/html/core-model-components.html#registered-client)
3. [OAuth Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
4. [Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
   - filter(request) => bearer token
   - provider manager -> jwt Authentication Provider(token) => [ jwt decoder, authentication converter ]
   - decoder(token) => [ decode, verify, validate ] => pass/ fail
   - converter(token) => Collection< Granted Authorities >
   - => JwtAuthenticationToken