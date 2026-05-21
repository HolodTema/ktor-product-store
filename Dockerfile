# BUILD STAGE
FROM gradle:8.5-jdk17 AS builder

WORKDIR /ktor-product-store

# copy files which are required for build and dependency resolving
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle
COPY src src

# make gradlew-file runnable via chmod command
RUN chmod +x gradlew

# build fat-JAR (using shadowJAR). For plain JAR replace to bootJar
# or assemble
RUN ./gradlew shadowJar --no-daemon

# RUNTIME STAGE
FROM openjdk:17-jre-slim

WORKDIR /app

# copy the JAR-file we created on the previous stage
COPY --from=builder /app/build/libs/*-all.jar app.jar

# By default, Ktor is listening 8080 port
EXPOSE 8080

# app launch
ENTRYPOINT ["java", "-jar", "app.jar"]
