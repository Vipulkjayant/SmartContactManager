# Use a lightweight Java image
FROM openjdk:17-jdk-slim

# Create and set working directory
WORKDIR /app

# Copy your built JAR into the image
COPY target/manager-0.0.1-SNAPSHOT.jar app.jar

# Expose the Spring Boot port
EXPOSE 9999

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
