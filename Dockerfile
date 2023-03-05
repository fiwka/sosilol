FROM eclipse-temurin:19-jdk
WORKDIR /app
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar ./
CMD bash -c "cd /app && java -jar sosilol-0.0.1-SNAPSHOT.jar"