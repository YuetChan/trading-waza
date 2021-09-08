FROM adoptopenjdk/openjdk9:latest
ARG JAR_FILE=target/eb-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} eb-portal.jar
ENTRYPOINT [ "java", "-jar", "eb-portal.jar" ]