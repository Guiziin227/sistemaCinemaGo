FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
COPY static ./static
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S cinema && adduser -S cinema -G cinema
COPY --from=build /workspace/target/cinema-1.0.0.jar app.jar
USER cinema
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
