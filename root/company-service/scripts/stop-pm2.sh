#!/usr/bin/env bash

# API Gateway Server 설정
COMPANY_SERVICE_NAME="company-service"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# PM2를 사용하여 API Gateway Server 종료
echo "$TIME_NOW > PM2를 사용하여 $COMPANY_SERVICE_NAME 프로세스 종료" >> $DEPLOY_LOG
pm2 stop $COMPANY_SERVICE_NAME