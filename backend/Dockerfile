# syntax=docker/dockerfile:1

FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn

COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src

ENV SPRING_PROFILES_ACTIVE=default
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]