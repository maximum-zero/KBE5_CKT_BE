# PostgreSQL 17 (Debian Bullseye 기반) 이미지를 베이스로 사용합니다.
FROM postgres:17-bullseye

# 환경 변수 설정
ENV PG_MAJOR 17
ENV TIMESCALEDB_APTVERSION 2
ENV POSTGIS_MAJOR 3

# 필요한 패키지 설치 (wget, gnupg 등) 및 TimescaleDB APT repository 추가
# apt-key add 대신 gpg --dearmor를 사용하여 GPG 키를 안전하게 추가합니다.
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        dirmngr \
        lsb-release \
        ca-certificates \
    && \
    # TimescaleDB GPG 키 다운로드 및 /usr/share/keyrings 디렉토리에 저장
    wget --quiet -O - https://packagecloud.io/timescale/timescaledb/gpgkey | gpg --dearmor -o /usr/share/keyrings/timescaledb-archive-keyring.gpg && \
    # TimescaleDB APT 저장소 설정 파일을 /etc/apt/sources.list.d 에 추가
    echo "deb [signed-by=/usr/share/keyrings/timescaledb-archive-keyring.gpg] https://packagecloud.io/timescale/timescaledb/debian/ $(lsb_release -c -s) main" > /etc/apt/sources.list.d/timescaledb.list && \
    apt-get update

# TimescaleDB, PostGIS, psql 클라이언트 등 실제 데이터베이스 관련 패키지 설치
RUN apt-get install -y --no-install-recommends \
    timescaledb-$TIMESCALEDB_APTVERSION-postgresql-$PG_MAJOR \
    postgresql-$PG_MAJOR-postgis-$POSTGIS_MAJOR \
    postgresql-$PG_MAJOR-postgis-$POSTGIS_MAJOR-scripts \
    postgresql-client-$PG_MAJOR \
    timescaledb-tools \
    postgresql-contrib \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# TimescaleDB 확장이 PostgreSQL 시작 시 미리 로드되도록 설정합니다.
RUN echo "shared_preload_libraries = 'timescaledb'" >> /usr/share/postgresql/$PG_MAJOR/postgresql.conf.sample

# docker-entrypoint-initdb.d 디렉토리 생성 및 스크립트 복사
# 이 스크립트는 컨테이너가 처음 시작될 때 자동으로 TimescaleDB와 PostGIS 확장을 활성화합니다.
RUN mkdir -p /docker-entrypoint-initdb.d
COPY ./initdb.d/init.sql /docker-entrypoint-initdb.d/init.sql
