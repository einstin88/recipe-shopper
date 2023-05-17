FROM maven:3.9.1-eclipse-temurin-17-alpine as maven
# First stage

WORKDIR /app/auth

COPY pom.xml ./
COPY src ./src

RUN ["mvn", "clean","-DskipTests", "package"]

# Second stage
FROM eclipse-temurin:17.0.7_7-jre-alpine

WORKDIR /app

COPY --from=maven /app/auth/target/jwt-auth-server-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080
CMD ["java", "-jar", "jwt-auth-server-0.0.1-SNAPSHOT.jar"]
