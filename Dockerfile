FROM eclipse-temurin:19-jdk
ARG JAR=sosilol-*.jar
COPY build/libs/${JAR} /app/sosilol.jar
COPY entrypoint.sh /app/entrypoint.sh
ENTRYPOINT ["/app/entrypoint.sh"]