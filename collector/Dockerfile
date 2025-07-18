FROM amazoncorretto:17
WORKDIR /app
COPY pinpoint-agent-2.5.4 /app/pinpoint-agent-2.5.4
COPY ./collector/build/libs/*.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java"]
