FROM openjdk:21-jdk-slim
WORKDIR /app
COPY src/main/resources/static /app/resources/static/
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]






