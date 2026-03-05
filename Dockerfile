FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/sep-project-0.0.1.jar
COPY ${JAR_FILE} app_sep.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_sep.jar"]