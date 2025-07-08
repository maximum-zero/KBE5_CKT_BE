# 사용할 기본 이미지 (Amazon Corretto 17)
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 1. Pinpoint Agent 파일 복사 (경로 수정!)
# 빌드 컨텍스트(./collector)의 상위 디렉토리(..)에 있는 pinpoint-agent-2.5.4 폴더를 복사합니다.
COPY pinpoint-agent-2.5.4 /app/pinpoint-agent-2.5.4

# 2. 빌드된 Spring Boot JAR 파일 복사
# 이 경로는 Dockerfile이 있는 ./collector 디렉토리 내부의 build/libs를 참조합니다.
COPY ./admin/build/libs/*.jar app.jar

# 3. 애플리케이션 실행 명령어 정의 (Pinpoint 에이전트 JVM 옵션 포함)
ENTRYPOINT ["java", \
            "-javaagent:/app/pinpoint-agent-2.5.4/pinpoint-bootstrap-2.5.4.jar", \
            "-Dpinpoint.agentId=${HOSTNAME}-admin-app", \
            "-Dpinpoint.applicationName=Ckt-Admin"]
