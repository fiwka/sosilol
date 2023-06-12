FROM eclipse-temurin:19-jdk-alpine
WORKDIR /app
COPY build/libs/sosilol-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-Xmx1024M", "-Xms1024M", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:+UnlockExperimentalVMOptions", "-XX:+PerfDisableSharedMem", "-XX:+DisableExplicitGC", "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M", "-jar", "sosilol-0.0.1-SNAPSHOT.jar"]
