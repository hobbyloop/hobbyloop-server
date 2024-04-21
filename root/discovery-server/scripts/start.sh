#!/usr/bin/env bash

# Discovery Server 설정
DISCOVERY_SERVER_ROOT="/home/ubuntu/app/discovery-server"
DISCOVERY_LATEST_JAR=$(ls -t /home/ubuntu/app/discovery-server/build/libs/*.jar | grep -v "plain" | head -1)
DISCOVERY_SERVER_JAR="$DISCOVERY_SERVER_ROOT/discovery-server.jar"
DISCOVERY_SERVER_APP_LOG="$DISCOVERY_SERVER_ROOT/discovery-server.log"
DISCOVERY_SERVER_ERROR_LOG="$DISCOVERY_SERVER_ROOT/discovery-server-error.log"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Discovery Server 배포 및 실행
echo "$TIME_NOW > $DISCOVERY_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
cp "$DISCOVERY_LATEST_JAR" "$DISCOVERY_SERVER_JAR"

echo "$TIME_NOW > $DISCOVERY_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
nohup java -jar $DISCOVERY_SERVER_JAR > $DISCOVERY_SERVER_APP_LOG 2> $DISCOVERY_SERVER_ERROR_LOG &