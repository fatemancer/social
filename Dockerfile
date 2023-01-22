FROM bellsoft/liberica-openjdk-alpine:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,address=0.0.0.0:5005,server=y,suspend=n", "-jar", "/app.jar"]