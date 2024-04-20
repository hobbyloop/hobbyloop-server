#!/usr/bin/env bash

# Discovery Server 설정
DISCOVERY_SERVER_ROOT="/home/ubuntu/app/discovery-server"
DISCOVERY_SERVER_JAR="$DISCOVERY_SERVER_ROOT/discovery-server.jar"

# API Gateway Server 설정
API_GATEWAY_SERVER_ROOT="/home/ubuntu/app/api-gateway-server"
API_GATEWAY_SERVER_JAR="$API_GATEWAY_SERVER_ROOT/api-gateway-server.jar"

# Company Service 설정
COMPANY_SERVICE_SERVER_ROOT="/home/ubuntu/app/company-service"
COMPANY_SERVICE_SERVER_JAR="$COMPANY_SERVICE_SERVER_ROOT/company-service.jar"

# Ticket Service 설정
TICKET_SERVICE_SERVER_ROOT="/home/ubuntu/app/ticket-service"
TICKET_SERVICE_SERVER_JAR="$TICKET_SERVICE_SERVER_ROOT/ticket-service.jar"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Discovery Server 종료
DISCOVERY_SERVER_PID=$(pgrep -f $DISCOVERY_SERVER_JAR)
if [ -z $DISCOVERY_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 Discovery Server가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $DISCOVERY_SERVER_PID Discovery Server 종료" >> $DEPLOY_LOG
    kill -15 $DISCOVERY_SERVER_PID
fi

# API Gateway Server 종료
API_GATEWAY_SERVER_PID=$(pgrep -f $API_GATEWAY_SERVER_JAR)
if [ -z $API_GATEWAY_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 API Gateway Server가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $API_GATEWAY_SERVER_PID API Gateway Server 종료" >> $DEPLOY_LOG
    kill -15 $API_GATEWAY_SERVER_PID
fi

# Company Service 종료
COMPANY_SERVICE_SERVER_PID=$(pgrep -f $COMPANY_SERVICE_SERVER_JAR)
if [ -z $COMPANY_SERVICE_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 Company Service가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $COMPANY_SERVICE_SERVER_PID Company Service 종료" >> $DEPLOY_LOG
    kill -15 $COMPANY_SERVICE_SERVER_PID
fi

# Ticket Service 종료
TICKET_SERVICE_SERVER_PID=$(pgrep -f $TICKET_SERVICE_SERVER_JAR)
if [ -z $TICKET_SERVICE_SERVER_PID ]; then
    echo "$TIME_NOW > 현재 실행 중인 Ticket Service가 없습니다" >> $DEPLOY_LOG
else
    echo "$TIME_NOW > 실행 중인 $TICKET_SERVICE_SERVER_PID Ticket Service 종료" >> $DEPLOY_LOG
    kill -15 $TICKET_SERVICE_SERVER_PID
fi