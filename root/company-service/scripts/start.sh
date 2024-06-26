#!/usr/bin/env bash

# Company Service Server 설정
COMPANY_SERVICE_SERVER_ROOT="/home/ubuntu/app/company-service"
COMPANY_SERVICE_LATEST_JAR=$(ls -t /home/ubuntu/app/company-service/build/libs/*.jar | grep -v "plain" | head -1)
COMPANY_SERVICE_SERVER_JAR="$COMPANY_SERVICE_SERVER_ROOT/company-service.jar"
COMPANY_SERVICE_SERVER_APP_LOG="$COMPANY_SERVICE_SERVER_ROOT/company-service.log"
COMPANY_SERVICE_SERVER_ERROR_LOG="$COMPANY_SERVICE_SERVER_ROOT/company-service-error.log"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Company Service 배포 및 실행
echo "$TIME_NOW > $COMPANY_SERVICE_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
cp "$COMPANY_SERVICE_LATEST_JAR" "$COMPANY_SERVICE_SERVER_JAR"

echo "$TIME_NOW > $COMPANY_SERVICE_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
nohup java -jar $COMPANY_SERVICE_SERVER_JAR > $COMPANY_SERVICE_SERVER_APP_LOG 2> $COMPANY_SERVICE_SERVER_ERROR_LOG &

COMPANY_SERVICE_SERVER_PID=$(pgrep -f $COMPANY_SERVICE_SERVER_JAR)
echo "$TIME_NOW > 실행된 Company Service 프로세스 아이디 $COMPANY_SERVICE_SERVER_PID 입니다." >> $DEPLOY_LOG
