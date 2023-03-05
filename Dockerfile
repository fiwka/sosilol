FROM eclipse-temurin:19-jdk
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar /app/
CMD bash -c "cd /app && java -jar sosilol-0.0.1-SNAPSHOT.jar"