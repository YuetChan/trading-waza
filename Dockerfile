FROM adoptopenjdk/openjdk9:latest
ARG JAR_FILE=target/tw-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} tw.jar
ENTRYPOINT [ "java", "-jar", "tw.jar" ]