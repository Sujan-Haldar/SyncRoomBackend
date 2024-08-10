# Stage 1: Build the Spring Boot application with Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# Stage 2: Run the Spring Boot application with Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080
EXPOSE 8081
# Run the Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]
