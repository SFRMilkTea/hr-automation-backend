FROM openjdk:17-jdk-alpine as extractor
RUN mkdir -p /opt
COPY build/libs/hr-automation-backend.jar /opt/app.jar
WORKDIR /opt
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17-jdk-alpine
WORKDIR /opt
COPY --from=extractor opt/dependencies/ ./
COPY --from=extractor opt/spring-boot-loader/ ./
COPY --from=extractor opt/snapshot-dependencies/ ./
COPY --from=extractor opt/application/ ./

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher"]

