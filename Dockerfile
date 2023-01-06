FROM bellsoft/liberica-openjdk-alpine:11
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
COPY "build/libs/highload-social-0.0.1-SNAPSHOT.jar" app.jar
ENTRYPOINT ["java","-jar","/app.jar"]