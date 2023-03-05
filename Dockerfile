FROM eclipse-temurin:19-jdk
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar /app/
COPY entrypoint.sh /app/
ENTRYPOINT ["/app/entrypoint.sh"]