# Stage 1: Build the Spring Boot application
FROM maven:3.9.2-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Prepare a Java 21 environment
FROM eclipse-temurin:21-jdk-alpine AS jdk

# Stage 3: Run the Spring Boot application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]



