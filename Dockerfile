FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# JAR 파일 복사
COPY build/libs/beyour.jar beyour.jar

# firebase-key.json 복사
COPY src/main/resources/firebase-key.json firebase-key.json

ENTRYPOINT ["java", "-jar", "beyour.jar"]
