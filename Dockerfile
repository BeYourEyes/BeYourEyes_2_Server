FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# JAR 파일 복사
COPY build/libs/beyour.jar beyour.jar

ENTRYPOINT ["java", "-jar", "beyour.jar"]
