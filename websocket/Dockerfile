FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY build/libs/websocket-0.0.1.jar /app/websocket.jar
ENTRYPOINT ["java","-jar","/app/websocket.jar"]