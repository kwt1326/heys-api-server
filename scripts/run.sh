PROJECT_NAME=api

JAR_NAME=$(ls -tr $PROJECT_NAME-* | grep jar | tail -n 1)

# kill running server

CURRENT_PID=$(pgrep -f $PROJECT_NAME-*.jar)

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
        -Dspring.profiles.active=prod \
        -Dspring.config.location=file:prod-application.yml \
        $JAR_NAME 2>&1 &