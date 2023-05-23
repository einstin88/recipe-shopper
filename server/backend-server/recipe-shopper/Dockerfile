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

ARG MYSQL_URL_BACKEND=jdbc:mysql://recipe_shopper?cloudSqlInstance=recipe-shopper-5d242:asia-southeast1:recipee-mysql&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=recipee-gke-service&ipTypes=PUBLIC&enableIamAuth=true
ARG SCRAPPER_URL=http://server-scrapper-1-service/api/
ARG KEY_URL=http://server-auth-1-service/auth/key-uri/

ENV MYSQL_URL_BACKEND=${MYSQL_URL_BACKEND}
ENV SCRAPPER_URL=${SCRAPPER_URL}
ENV KEY_URL=${KEY_URL}

EXPOSE 8080
CMD ["java", "-jar", "recipe-shopper-0.0.1-SNAPSHOT.jar"]
