# Build: Java 25 official image
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app


# Copy the maven-generated .jar in container
COPY target/Clinica-medicala-Server-1.0-SNAPSHOT.jar app.jar

# Start server
ENTRYPOINT ["java", "-jar", "app.jar"]