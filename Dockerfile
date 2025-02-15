# 🛠 Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the project files to the container
COPY . .

# Give execution permission to Maven Wrapper (if applicable)
RUN chmod +x mvnw

# Build the application and skip tests
RUN mvn clean package -DskipTests

# 🏗 Stage 2: Run the application using OpenJDK
FROM openjdk:17.0.1-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Set the default command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
