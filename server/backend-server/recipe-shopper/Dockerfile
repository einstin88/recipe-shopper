FROM maven:3.9.1-eclipse-temurin-17-alpine as maven
# First stage - package to JAR

WORKDIR /app/backend

COPY pom.xml ./
COPY src ./src

RUN ["mvn", "clean","-DskipTests", "package"]

# Second stage - setup runtime environment
FROM eclipse-temurin:17.0.7_7-jre-alpine

WORKDIR /app

COPY --from=maven /app/backend/target/recipe-shopper-0.0.1-SNAPSHOT.jar ./

ARG MYSQL_URL_BACKEND
ARG SCRAPPER_URL
ARG KEY_URL
ARG GOOGLE_CREDENTIALS

ENV MYSQL_URL_BACKEND=${MYSQL_URL_BACKEND}
ENV SCRAPPER_URL=${SCRAPPER_URL}
ENV KEY_URL=${KEY_URL}
ENV GOOGLE_CREDENTIALS=${GOOGLE_CREDENTIALS}

EXPOSE 8080
CMD ["java", "-jar", "recipe-shopper-0.0.1-SNAPSHOT.jar"]
