#!/usr/bin/env bash

# Ticket Service Server 설정
TICKET_SERVICE_SERVER_ROOT="/home/ubuntu/app/ticket-service"
TICKET_SERVICE_LATEST_JAR=$(ls -t /home/ubuntu/app/ticket-service/build/libs/*.jar | grep -v "plain" | head -1)
TICKET_SERVICE_SERVER_JAR="$TICKET_SERVICE_SERVER_ROOT/ticket-service.jar"
TICKET_SERVICE_SERVER_APP_LOG="$TICKET_SERVICE_SERVER_ROOT/ticket-service.log"
TICKET_SERVICE_SERVER_ERROR_LOG="$TICKET_SERVICE_SERVER_ROOT/ticket-service-error.log"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Ticket Service 배포 및 실행
echo "$TIME_NOW > $TICKET_SERVICE_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
cp "$TICKET_SERVICE_LATEST_JAR" "$TICKET_SERVICE_SERVER_JAR"

echo "$TIME_NOW > $TICKET_SERVICE_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
nohup java -jar $TICKET_SERVICE_SERVER_JAR > $TICKET_SERVICE_SERVER_APP_LOG 2> $TICKET_SERVICE_SERVER_ERROR_LOG &

TICKET_SERVICE_SERVER_PID=$(pgrep -f $TICKET_SERVICE_SERVER_JAR)
echo "$TIME_NOW > 실행된 Ticket Service 프로세스 아이디 $TICKET_SERVICE_SERVER_PID 입니다." >> $DEPLOY_LOG