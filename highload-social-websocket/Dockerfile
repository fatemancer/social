FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . src
WORKDIR src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY build/libs/highload-social-websocket-all.jar /app/ktor-websocket.jar
ENTRYPOINT ["java","-jar","/app/ktor-websocket.jar"]