#!/usr/bin/env bash

# API Gateway Server 설정
API_GATEWAY_SERVER_ROOT="/home/ubuntu/app/api-gateway-server"
API_GATEWAY_SERVER_JAR="$API_GATEWAY_SERVER_ROOT/api-gateway-server.jar"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# API Gateway Server 종료
API_GATEWAY_SERVER_PID=$(pgrep -f $API_GATEWAY_SERVER_JAR)
if [ -z $API_GATEWAY_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 API Gateway Server가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $API_GATEWAY_SERVER_PID API Gateway Server 종료" >> $DEPLOY_LOG
    kill -15 $API_GATEWAY_SERVER_PID
fi