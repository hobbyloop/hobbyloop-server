#!/usr/bin/env bash

# Discovery Server 설정
DISCOVERY_SERVER_NAME="discovery-server"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

echo "$TIME_NOW > PM2를 사용하여 $DISCOVERY_SERVER_NAME 프로세스 종료" >> $DEPLOY_LOG
pm2 stop $DISCOVERY_SERVER_NAME