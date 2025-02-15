# 기본 이미지
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY build/libs/book-rental-app.jar app.jar

# 실행 명령어
CMD ["java", "-jar", "app.jar"]