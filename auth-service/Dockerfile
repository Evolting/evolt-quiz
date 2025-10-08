FROM maven:3.9.11-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER eazybytes.com
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="eazybytes.com"

WORKDIR /app

# Install curl
RUN apt-get update && apt-get install -y curl

# Copy the built JAR file from the 'build' stage to the final image
COPY --from=build /app/target/auth-service.jar .

# execute the application
ENTRYPOINT ["java", "-jar", "auth-service.jar"]