PROJECT_NAME=heys-api-server

JAR_NAME=$(ls -tr *$PROJECT_NAME | grep jar | tail -n 1)

nohup java -jar \
        -Dspring.profiles.active=prod \
        -Dspring.config.location=file:prod-application.yml \
        $JAR_NAME 2>&1 &