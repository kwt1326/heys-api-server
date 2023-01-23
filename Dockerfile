# JAR BUILD STAGE
# DB_HOST_URL format: $DB_HOST:$DB_PORT/$DB_NAME
FROM --platform=arm64 eclipse-temurin:17 AS builder
ARG ENVIRONMENT=prod
ENV PORT=8090
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar
COPY ./build/libs/*.jar deploy-jar.jar
EXPOSE $PORT
RUN ls -al
ENTRYPOINT [ "java", "-jar", "-Dspring.config.location=file:$ENVIRONMENT-application.yml", "deploy-jar.jar" ]