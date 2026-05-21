FROM eclipse-temurin:17-jre-slim

WORKDIR /ktor-product-store

COPY build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

