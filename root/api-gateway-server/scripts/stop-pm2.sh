#!/usr/bin/env bash

# API Gateway Server 설정
API_GATEWAY_SERVER_NAME="api-gateway-server"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# PM2를 사용하여 API Gateway Server 종료
echo "$TIME_NOW > PM2를 사용하여 $API_GATEWAY_SERVER_NAME 프로세스 종료" >> $DEPLOY_LOG
pm2 stop $API_GATEWAY_SERVER_NAME