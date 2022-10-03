PROJECT_NAME=heys-api-server

cp ../build/libs/*.jar ..

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

if [ -z "$CURRENT_PID" ]; then
        echo "*** current not running application, try run app ***"
else
        echo "*** kill -15 $CURRENT_PID ***"
        kill -15 $CURRENT_PID
        sleep 5
fi