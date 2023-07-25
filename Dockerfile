# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Download and cache the dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src/ ./src/

# Build the application
RUN mvn package

# Stage 2: Create the final runtime image
FROM alpine:latest

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/GymTracker-0.0.1-SNAPSHOT.jar ./gym-tracker-backend.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "my-maven-docker-project.jar"]

# Expose the necessary port
EXPOSE 8080
