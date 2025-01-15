FROM openjdk:17.0.2
LABEL authors="isafonov"
ARG JAR_PATH

COPY $JAR_PATH/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
