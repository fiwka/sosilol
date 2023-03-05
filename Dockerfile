FROM eclipse-temurin:19-jdk
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar /app/
COPY entrypoint.sh /app/
WORKDIR /app
ENTRYPOINT ["bash", "entrypoint.sh"]