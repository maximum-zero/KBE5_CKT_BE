# 사용할 기본 이미지
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 1. Pinpoint Agent 파일 복사
# 이제 pinpoint-agent-2.5.4 폴더가 Dockerfile과 같은 디렉토리(빌드 컨텍스트의 루트)에 있습니다.
COPY pinpoint-agent-2.5.4 /app/pinpoint-agent-2.5.4

# 2. 빌드된 Spring Boot JAR 파일 복사
# 이 경로는 Dockerfile과 같은 디렉토리(./collector) 안에 build/libs가 있으므로 이 경로는 그대로 유지됩니다.
COPY ./collector/build/libs/*.jar app.jar

# 3. 애플리케이션 실행 명령어 정의 (Pinpoint 에이전트 JVM 옵션 포함)
ENTRYPOINT ["java", \
            "-javaagent:/app/pinpoint-agent-2.5.4/pinpoint-bootstrap-2.5.4.jar", \
            "-Dpinpoint.agentId=${HOSTNAME}-collector-app", \
            "-Dpinpoint.applicationName=Ckt-Collector"]
