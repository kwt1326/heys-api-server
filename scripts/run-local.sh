#!/bin/bash +x

PROJECT_NAME=heys-api-server
JAR_NAME=heys-api-server.jar

chmod +x ./gradlew

./gradlew build

cp ./build/libs/*.jar .

mv $(ls -tr ${PROJECT_NAME}* | grep jar | tail -n 1) $JAR_NAME

echo "JAR NAME = $JAR_NAME"

# kill running server
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
        echo "*** current not running application, try run app ***"
else
        echo "*** kill -15 $CURRENT_PID ***"
        kill -15 $CURRENT_PID
        sleep 5
fi

# run jar file

echo "*** build jar name : $JAR_NAME ***"

nohup java -jar \
        -Dspring.config.location=file:local-application.yml \
        -Duser.timezone=Asia/Seoul \
        $JAR_NAME > nohup.txt 2>&1 &

echo "Running Local"