#!/usr/bin/env bash

# Discovery Server 설정
DISCOVERY_SERVER_ROOT="/home/ubuntu/app/discovery-server"
DISCOVERY_SERVER_JAR="$DISCOVERY_SERVER_ROOT/discovery-server.jar"

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