#!/bin/bash
export TZ="Asia/Seoul"

NOW=$(date +%c)

CONTAINER_ID=$(docker container ls -f "name=health_genie_1" -q)

echo "[$NOW] > 컨테이너 ID: ${CONTAINER_ID}" >> /home/ubuntu/cicd/start.log

if [ -z ${CONTAINER_ID} ]
then
  echo "[$NOW] > 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/cicd/deploy.log
else
  echo "[$NOW] > docker stop ${CONTAINER_ID}"
  sudo docker stop ${CONTAINER_ID}
  echo "[$NOW] > docker rm ${CONTAINER_ID}"
  sudo docker rm ${CONTAINER_ID}
  echo "[$NOW] > docker rmi -f j9972/health_genie "
  sudo docker rmi -f j9972/health_genie
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

echo "[$NOW] > health genie server start!! welcome to Health- Genie "