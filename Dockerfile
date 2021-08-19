FROM openjdk:8-jdk-alpine
EXPOSE 8080
WORKDIR /app
COPY target/eb-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "eb-0.0.1-SNAPSHOT.jar" ]