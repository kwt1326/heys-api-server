FROM eclipse-temurin:17
EXPOSE 8090
ARG ENV=prod
ENV ENVIRONMENT=$ENV
ENV JAR_NAME=heys-api-server.jar
COPY build/libs/\*.jar $JAR_NAME
COPY *-application.yml .
RUN java -version
RUN echo $ENVIRONMENT
CMD java -jar -Dspring.config.location=file:$ENVIRONMENT-application.yml $JAR_NAME