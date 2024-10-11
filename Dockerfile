FROM openjdk:8-jdk-alpine
LABEL authors="thivernale"
ARG JAR_FILE=target/*.jar
COPY $JAR_FILE app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
