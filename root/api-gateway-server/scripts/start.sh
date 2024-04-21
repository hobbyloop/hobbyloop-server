#!/usr/bin/env bash

# Api Gateway Server 설정
API_GATEWAY_SERVER_ROOT="/home/ubuntu/app/api-gateway-server"
API_GATEWAY_LATEST_JAR=$(ls -t /home/ubuntu/app/api-gateway-server/build/libs/*.jar | grep -v "plain" | head -1)
API_GATEWAY_SERVER_JAR="$API_GATEWAY_SERVER_ROOT/api-gateway-server.jar"
API_GATEWAY_SERVER_APP_LOG="$API_GATEWAY_SERVER_ROOT/api-gateway-server.log"
API_GATEWAY_SERVER_ERROR_LOG="$API_GATEWAY_SERVER_ROOT/api-gateway-server-error.log"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# API Gateway Server 배포 및 실행
echo "$TIME_NOW > $API_GATEWAY_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
cp "$API_GATEWAY_LATEST_JAR" "$API_GATEWAY_SERVER_JAR"

echo "$TIME_NOW > $API_GATEWAY_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
nohup java -jar $API_GATEWAY_SERVER_JAR > $API_GATEWAY_SERVER_APP_LOG 2> $API_GATEWAY_SERVER_ERROR_LOG &

API_GATEWAY_SERVER_PID=$(pgrep -f $API_GATEWAY_SERVER_JAR)
echo "$TIME_NOW > 실행된 API Gateway Server 프로세스 아이디 $API_GATEWAY_SERVER_PID 입니다." >> $DEPLOY_LOG