# Stage 1: Build the app using Maven
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app using a lightweight JDK
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]

