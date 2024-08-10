FROM openjdk:18
WORKDIR /app
COPY ./target/Discord_Backend-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "spring-0.0.1-SNAPSHOT.jar"]

