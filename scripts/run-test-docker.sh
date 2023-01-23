#!/bin/bash +x

JAR_NAME=deploy-jar.jar
DOCKER_REPO=kwt1326
DOCKER_IMAGE_NAME=heys-api-server
DOCKER_FILE=Dockerfile
IMAGE_TAG=1.0.0
PORT=8090
ENVIRONMENT=local
SECRET=scDEVdjkjqowjioejowi129232j9hj2h9334587klfj489fj85489gh389fj398j89j34f9j9834jf9834fj9438h439f
DB_HOST_URL=jdbc:postgresql://localhost:5432/heys
DB_USER=root
DB_PASSWORD=
AWS_ACCESS_KEY=test
AWS_SECRET_KEY=test1

docker build \
  -t $DOCKER_REPO/$DOCKER_IMAGE_NAME:$IMAGE_TAG \
  -f $DOCKER_FILE \
  --progress=plain \
  --build-arg ENVIRONMENT=$ENVIRONMENT \
  --build-arg PORT=$PORT . \
  --build-arg SECRET=$SECRET \
  --build-arg DB_HOST_URL=$DB_HOST_URL \
  --build-arg JAR_NAME=$JAR_NAME \
  --build-arg DB_USER=$DB_USER \
  --build-arg DB_PASSWORD=$DB_PASSWORD \
  --build-arg AWS_ACCESS_KEY=$AWS_ACCESS_KEY \
  --build-arg AWS_SECRET_KEY=$AWS_SECRET_KEY \

if [ $? -eq 0 ]; then
  echo "Docker Build Success"
else
  echo "Docker Build Fail"
  exit 1
fi

# kill running server
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "*** current not running application, try run app ***"
else
  echo "*** kill -15 $CURRENT_PID ***"
  kill -15 $CURRENT_PID
  sleep 5
fi

docker run --rm -p $PORT:$PORT $DOCKER_REPO/$DOCKER_IMAGE_NAME:$IMAGE_TAG

# docker push $DOCKER_REPO/$DOCKER_IMAGE_NAME:$IMAGE_TAG