#!/bin/bash

JAR_NAME="hffv-airwave-dp-mockapi"
HOSTNAME="0.0.0.0"
PORT=9090
PID_FILE="airwave-dp-mockapi.pid"


case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    check)
        check
        ;;
    *)
        echo "Usage: $0 {start|stop|check}"
        exit 1
        ;;
esac

start() {
    if [ -f "$PID_FILE" ]; then
        echo "Airwave DP Mock API is already running (PID: $(cat $PID_FILE))."
        exit 1
    fi

    echo "Starting Airwave DP Mock API on host $HOSTNAME and port $PORT..."
    nohup java -Dhostname=$HOSTNAME -Dport=$PORT -jar $JAR_NAME > airwave-dp-mockapi.log 2>&1 &

    # $! is the PID of the last program shell ran in the background
    echo $! > "$PID_FILE"
    echo "Airwave DP Mock API started (PID: $(cat $PID_FILE))."
}

stop() {
    if [ ! -f "$PID_FILE" ]; then
        echo "Airwave DP Mock API is not running."
        exit 1
    fi

    echo "Stopping Airwave DP Mock API..."
    kill "$(cat $PID_FILE)"
    rm -f "$PID_FILE"
    echo "Airwave DP Mock API stopped."
}

check() {
    if [ -f "$PID_FILE" ]; then
        echo "Airwave DP Mock API is running (PID: $(cat $PID_FILE))."
    else
        echo "Airwave DP Mock API is not running."
    fi
}


#!/bin/bash

# Configuration
APP_NAME="wiremock-mock-server"
JAR_DIR="/app/${APP_NAME}/jar"
LOG_DIR="/app/${APP_NAME}/log"
JAR_FILE="${JAR_DIR}/${APP_NAME}.jar"
LOG_FILE="${LOG_DIR}/${APP_NAME}.log"
PORT=8080  # Change this if needed

# Ensure log directory exists
mkdir -p "${LOG_DIR}"

# Get the process count for the JAR
get_process_count() {
    pgrep -f "${JAR_FILE}" | wc -l
}

start() {
    if [ "$(get_process_count)" -gt 0 ]; then
        echo "${APP_NAME} is already running."
    else
        echo "Starting ${APP_NAME}..."
        nohup java -jar "${JAR_FILE}" --server.port=${PORT} > "${LOG_FILE}" 2>&1 &
        sleep 2
        if [ "$(get_process_count)" -gt 0 ]; then
            echo "${APP_NAME} started successfully."
        else
            echo "Failed to start ${APP_NAME}."
        fi
    fi
}

stop() {
    if [ "$(get_process_count)" -gt 0 ]; then
        echo "Stopping ${APP_NAME}..."
        pkill -f "${JAR_FILE}"
        sleep 2
        if [ "$(get_process_count)" -eq 0 ]; then
            echo "${APP_NAME} stopped successfully."
        else
            echo "Failed to stop ${APP_NAME}."
        fi
    else
        echo "${APP_NAME} is not running."
    fi
}

check() {
    if [ "$(get_process_count)" -gt 0 ]; then
        echo "${APP_NAME} is running."
    else
        echo "${APP_NAME} is not running."
    fi
}

case "$1" in
    -start)
        start
        ;;
    -stop)
        stop
        ;;
    -check)
        check
        ;;
    *)
        echo "Usage: $0 {-start|-stop|-check}"
        exit 1
        ;;
esac
