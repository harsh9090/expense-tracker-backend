# Use OpenJDK runtime
FROM maven:3.8.5-openjdk-17 AS build

# Copy only the project folder (if needed)
COPY . .

RUN mvn clean install -DskipTests


FROM container-registry.oracle.com/java/openjdk:23
COPY --from=build /target/*.jar demo.jar

# Expose port 8080
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "demo.jar" ]
