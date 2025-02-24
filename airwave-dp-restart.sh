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
