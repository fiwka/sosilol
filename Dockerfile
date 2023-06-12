FROM eclipse-temurin:19-jdk-alpine
WORKDIR /app
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "sosilol-0.0.1-SNAPSHOT.jar"]
