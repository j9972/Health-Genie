##!/bin/bash
#
#ROOT_PATH="/home/ubuntu/cicd"
#JAR="$ROOT_PATH/app.jar"
#
#APP_LOG="$ROOT_PATH/application.log"
#ERROR_LOG="$ROOT_PATH/error.log"
#START_LOG="$ROOT_PATH/start.log"
#
#NOW=$(date +%c)
#
#echo "[$NOW] $JAR 복사" >> $START_LOG
#cp $ROOT_PATH/build/libs/*.jar $JAR
#
#echo "[$NOW] > $JAR 실행" >> $START_LOG
#nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &
#
#SERVICE_PID=$(pgrep -f $JAR)
#echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG

#!/bin/bash
CONTAINER_ID=$(docker container ls -f "name=health_genie_1" -q)

echo "> 컨테이너 ID: ${CONTAINER_ID}" >> /home/ubuntu/cicd/start.log

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/cicd/deploy.log
else
  echo "> docker stop ${CONTAINER_ID}"
  sudo docker stop ${CONTAINER_ID}
  echo "> docker rm ${CONTAINER_ID}"
  sudo docker rm ${CONTAINER_ID}
  sleep 5
fi

export ENCRYPTED_PASSWORD_FILE="/home/ubuntu/cicd/jasypt.txt"
#cd /home/ubuntu/cicd && docker build -t yapp .
docker run \
  --name=health_genie_1 \
  --restart unless-stopped \
  -e JAVA_OPTS=-Djasypt.encryptor.password=$(cat "$ENCRYPTED_PASSWORD_FILE") \
  -e TZ=Asia/Seoul \
  -p 1234:1234 \
  -d \
  --net mybridge \
  j9972/health_genie