FROM eclipse-temurin:19-jdk
ARG JAR=sosilol-*.jar
COPY build/libs/${JAR} /app/sosilol.jar
RUN bash -c "cd /app && java -jar sosilol.jar"