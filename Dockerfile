# Use OpenJDK runtime
FROM maven:3.8.5-openjdk-17 AS build

# Copy only the project folder (if needed)
COPY . .

RUN mvn clean install -DskipTests
# Give execution permission to mvnw
RUN chmod +x mvnw

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar demo.jar

# Expose port 8080
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "demo.jar" ]
