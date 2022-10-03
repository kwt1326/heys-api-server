PROJECT_NAME=api

chmod +x ./gradlew

./gradlew build

cp ./build/libs/*.jar .

mv $(ls -tr ${PROJECT_NAME}-* | grep jar | tail -n 1) deploy-jar.jar