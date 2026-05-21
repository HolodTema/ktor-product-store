FROM amazoncorretto:21-alpine

WORKDIR /ktor-product-store

COPY ./*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

