FROM eclipse-temurin:25-jdk AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /app

# Generate .jar with maven build
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/Server-module/target/Clinica-medicala-Server-1.0-SNAPSHOT.jar app.jar

# Start server
ENTRYPOINT ["java", "-jar", "app.jar"]