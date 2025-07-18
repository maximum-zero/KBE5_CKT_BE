FROM amazoncorretto:17
WORKDIR /app
COPY pinpoint-agent-2.5.4 /app/pinpoint-agent-2.5.4
COPY ./gateway/build/libs/*.jar app.jar
ENTRYPOINT ["java"]
