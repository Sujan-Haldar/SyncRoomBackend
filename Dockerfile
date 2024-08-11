# Stage 1: Build the Spring Boot application
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean install -U

# Stage 2: Run the Spring Boot application
FROM openjdk:21
WORKDIR /app
COPY --from=build /target/*.jar /app.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080
EXPOSE 8081
# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]

