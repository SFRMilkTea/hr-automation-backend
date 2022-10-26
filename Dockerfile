# syntax=docker/dockerfile:1

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/hr-automation-backend.jar /app.jar
CMD ["./gradlew", "clean", "build"]
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

