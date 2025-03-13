# 가벼운 JDK 21 이미지 사용
FROM eclipse-temurin:21-jdk-alpine

# 컨테이너 내부의 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사 (`beyour.jar` 사용)
COPY build/libs/beyour.jar beyour.jar

# 컨테이너 실행 시 명령어 설정 (beyour.jar 실행)
ENTRYPOINT ["java", "-jar", "beyour.jar"]
