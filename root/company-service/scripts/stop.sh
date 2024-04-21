#!/usr/bin/env bash

# Company Service 설정
COMPANY_SERVICE_SERVER_ROOT="/home/ubuntu/app/company-service"
COMPANY_SERVICE_SERVER_JAR="$COMPANY_SERVICE_SERVER_ROOT/company-service.jar"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Company Service 종료
COMPANY_SERVICE_SERVER_PID=$(pgrep -f $COMPANY_SERVICE_SERVER_JAR)
if [ -z $COMPANY_SERVICE_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 Company Service가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $COMPANY_SERVICE_SERVER_PID Company Service 종료" >> $DEPLOY_LOG
    kill -15 $COMPANY_SERVICE_SERVER_PID
fi