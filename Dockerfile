FROM eclipse-temurin:19-jdk
COPY build/libs/sosilol-*.jar /app/sosilol.jar
COPY entrypoint.sh /app/entrypoint.sh
ENTRYPOINT ["/app/entrypoint.sh"]