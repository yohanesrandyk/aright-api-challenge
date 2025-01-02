# syntax=docker/dockerfile:1

# Use the Eclipse Temurin base image with JDK to build the WAR file
FROM eclipse-temurin:11-jdk-jammy as builder

WORKDIR /build

# Copy the mvnw wrapper with executable permissions
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml ./

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw

# Download dependencies
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

# Copy application source code and package it into a WAR file
COPY src/ src/
RUN ./mvnw package -DskipTests && \
    mv target/*.war /app.war

################################################################################

# Use the official Tomcat image as the base for the runtime environment
FROM tomcat:9.0-jdk11 AS runtime
# FROM tomcat:9

# Copy the WAR file to Tomcat's webapps directory
COPY --from=builder /app.war /usr/local/tomcat/webapps/
RUN ls /usr/local/tomcat/webapps/
# ADD /app.war /usr/local/tomcat/webapps/

# Expose port 8080 for accessing the Tomcat server
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
