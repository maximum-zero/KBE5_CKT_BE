FROM amazoncorretto:17
WORKDIR /app
COPY ./collector/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
