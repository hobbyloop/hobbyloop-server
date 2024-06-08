#!/usr/bin/env bash

# Discovery Server 설정
DISCOVERY_SERVER_ROOT="/home/ubuntu/app/discovery"
DISCOVERY_SERVER_JAR="$DISCOVERY_SERVER_ROOT/discovery-server.jar"
DISCOVERY_SERVER_APP_LOG="$DISCOVERY_SERVER_ROOT/discovery-server.log"
DISCOVERY_SERVER_ERROR_LOG="$DISCOVERY_SERVER_ROOT/discovery-server-error.log"

# Api Gateway Server 설정
API_GATEWAY_SERVER_ROOT="/home/ubuntu/app/api-gateway"
API_GATEWAY_SERVER_JAR="$API_GATEWAY_SERVER_ROOT/api-gateway-server.jar"
API_GATEWAY_SERVER_APP_LOG="$API_GATEWAY_SERVER_ROOT/api-gateway-server.log"
API_GATEWAY_SERVER_ERROR_LOG="$API_GATEWAY_SERVER_ROOT/api-gateway-server-error.log"

# Company Service Server 설정
COMPANY_SERVICE_SERVER_ROOT="/home/ubuntu/app/company-service"
COMPANY_SERVICE_SERVER_JAR="$COMPANY_SERVICE_SERVER_ROOT/company-service.jar"
COMPANY_SERVICE_SERVER_APP_LOG="$COMPANY_SERVICE_SERVER_ROOT/company-service.log"
COMPANY_SERVICE_SERVER_ERROR_LOG="$COMPANY_SERVICE_SERVER_ROOT/company-service-error.log"

# Ticket Service Server 설정
TICKET_SERVICE_SERVER_ROOT="/home/ubuntu/app/ticket-service"
TICKET_SERVICE_SERVER_JAR="$TICKET_SERVICE_SERVER_ROOT/ticket-service.jar"
TICKET_SERVICE_SERVER_APP_LOG="$TICKET_SERVICE_SERVER_ROOT/ticket-service.log"
TICKET_SERVICE_SERVER_ERROR_LOG="$TICKET_SERVICE_SERVER_ROOT/ticket-service-error.log"

DEPLOY_LOG="/home/ubuntu/app/deploy.log"
TIME_NOW=$(date +%c)

# Discovery Server 배포 및 실행
echo "$TIME_NOW > $DISCOVERY_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
cp $DISCOVERY_SERVER_ROOT/build/libs/*.jar $DISCOVERY_SERVER_JAR

echo "$TIME_NOW > $DISCOVERY_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
nohup java -jar $DISCOVERY_SERVER_JAR > $DISCOVERY_SERVER_APP_LOG 2> $DISCOVERY_SERVER_ERROR_LOG &

# Discovery Server가 실행되는지 확인
sleep 10  # 서버가 시작될 시간을 기다립니다.
DISCOVERY_SERVER_PID=$(pgrep -f $DISCOVERY_SERVER_JAR)

if [ -z "$DISCOVERY_SERVER_PID" ]; then
  echo "$TIME_NOW > Discovery Server 실행 실패" >> $DEPLOY_LOG
  exit 1
else
  echo "$TIME_NOW > 실행된 Discovery Server 프로세스 아이디 $DISCOVERY_SERVER_PID 입니다." >> $DEPLOY_LOG

  # API Gateway Server 배포 및 실행
  echo "$TIME_NOW > $API_GATEWAY_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
  cp $API_GATEWAY_SERVER_ROOT/build/libs/*.jar $API_GATEWAY_SERVER_JAR

  echo "$TIME_NOW > $API_GATEWAY_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
  nohup java -jar $API_GATEWAY_SERVER_JAR > $API_GATEWAY_SERVER_APP_LOG 2> $API_GATEWAY_SERVER_ERROR_LOG &

  API_GATEWAY_SERVER_PID=$(pgrep -f $API_GATEWAY_SERVER_JAR)
  echo "$TIME_NOW > 실행된 API Gateway Server 프로세스 아이디 $API_GATEWAY_SERVER_PID 입니다." >> $DEPLOY_LOG

  # Company Service 배포 및 실행
  echo "$TIME_NOW > $COMPANY_SERVICE_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
  cp $COMPANY_SERVICE_SERVER_ROOT/build/libs/*.jar $COMPANY_SERVICE_SERVER_JAR

  echo "$TIME_NOW > $COMPANY_SERVICE_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
  nohup java -jar $COMPANY_SERVICE_SERVER_JAR > $COMPANY_SERVICE_SERVER_APP_LOG 2> $COMPANY_SERVICE_SERVER_ERROR_LOG &

  COMPANY_SERVICE_SERVER_PID=$(pgrep -f $COMPANY_SERVICE_SERVER_JAR)
  echo "$TIME_NOW > 실행된 Company Service 프로세스 아이디 $COMPANY_SERVICE_SERVER_PID 입니다." >> $DEPLOY_LOG

  # Ticket Service 배포 및 실행
  echo "$TIME_NOW > $TICKET_SERVICE_SERVER_JAR 파일 복사" >> $DEPLOY_LOG
  cp $TICKET_SERVICE_SERVER_ROOT/build/libs/*.jar $TICKET_SERVICE_SERVER_JAR

  echo "$TIME_NOW > $TICKET_SERVICE_SERVER_JAR 파일 실행" >> $DEPLOY_LOG
  nohup java -jar $TICKET_SERVICE_SERVER_JAR > $TICKET_SERVICE_SERVER_APP_LOG 2> $TICKET_SERVICE_SERVER_ERROR_LOG &

  TICKET_SERVICE_SERVER_PID=$(pgrep -f $TICKET_SERVICE_SERVER_JAR)
  echo "$TIME_NOW > 실행된 Ticket Service 프로세스 아이디 $TICKET_SERVICE_SERVER_PID 입니다." >> $DEPLOY_LOG
fi