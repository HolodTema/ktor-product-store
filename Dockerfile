FROM amazoncorretto:17-alpine

WORKDIR /ktor-product-store

COPY build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

