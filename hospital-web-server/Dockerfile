# Use a base image with Maven and JDK installed for building
FROM maven:3.8.4-openjdk-8 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy just the pom.xml file to the container
COPY pom.xml .

# Resolve project dependencies
RUN mvn dependency:go-offline

# Copy the entire source code to the container
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lightweight base image for the application runtime
FROM amazoncorretto:8

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built in the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
