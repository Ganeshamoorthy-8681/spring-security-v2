# Use OpenJDK 23 as base image since the app uses Java 23
FROM openjdk:23-jdk-slim

# Set working directory
WORKDIR /app

# Copy Gradle wrapper files
#COPY gradlew .
#COPY gradle/ gradle/

# Make gradlew executable
#RUN chmod +x ./gradlew

# Copy build files
#COPY build.gradle .
#COPY settings.gradle .

# Copy source code
#COPY src/ src/

# Build the application
#RUN ./gradlew build -x test --no-daemon


# Expose the port the app runs on (default Spring Boot port)
EXPOSE 8080

# Set environment variables with defaults (can be overridden at runtime)
#ENV DATASOURCE_URL=jdbc:postgresql://postgres:5432/auth
#ENV DATASOURCE_USERNAME=ganesh
#ENV DATASOURCE_PASSWORD=password
#ENV JWT_SECRET_KEY=dGhpcyBpcyBhIHNlY3JldCBrZXkgdGhhdCBpcyByYW5kb20gdGFuZCBsaWZlIFNlY3VyZSBpbiBqd3QgdG9rZW5z
#ENV UI_BASE_URL=http://localhost:4173

# Run the application
CMD ["java", "-jar", "/app/build/libs/spring-security-0.0.1-SNAPSHOT.jar"]
