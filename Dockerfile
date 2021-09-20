FROM adoptopenjdk/openjdk9:latest
ARG JAR_FILE=target/tb-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} tb.jar
ENTRYPOINT [ "java", "-jar", "tb.jar" ]