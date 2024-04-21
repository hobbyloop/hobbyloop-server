#!/usr/bin/env bash

# Ticket Service 설정
TICKET_SERVICE_SERVER_ROOT="/home/ubuntu/app/ticket-service"
TICKET_SERVICE_SERVER_JAR="$TICKET_SERVICE_SERVER_ROOT/ticket-service.jar"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Ticket Service 종료
TICKET_SERVICE_SERVER_PID=$(pgrep -f $TICKET_SERVICE_SERVER_JAR)
if [ -z $TICKET_SERVICE_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 Ticket Service가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $TICKET_SERVICE_SERVER_PID Ticket Service 종료" >> $DEPLOY_LOG
    kill -15 $TICKET_SERVICE_SERVER_PID
fi