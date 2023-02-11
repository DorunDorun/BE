# switch.sh

#!/bin/bash

# service_url.inc에서 현재 서비스를 하고 있는 WAS의 포트 번호 가져오기
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
    exit 1
fi

# 위 커맨드들을 통해 현재 타겟 포트 가져오기

# $ service_url.inc 파일을 현재 바뀐 서버의 포트로 변경
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | tee /home/ubuntu/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

# nginx를 reload 해준다.
sudo service nginx reload

echo "> Nginx reloaded."

# -9 SIGKILL 은 서버를 바로 종료하므로
# -15 SIGTERM  안전 종료인 SIGTERM을 사용하여 이전 포트 프로세스를 제거한다.

CURRENT_PID=$(lsof -Fp -i TCP:${CURRENT_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

sudo kill -15 ${CURRENT_PID}