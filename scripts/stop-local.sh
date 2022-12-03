JAR_NAME=deploy-jar.jar

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
        echo "*** current not running application, Done ***"
else
        echo "*** kill -15 $CURRENT_PID, Done ***"
        kill -15 $CURRENT_PID
fi