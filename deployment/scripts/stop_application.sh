#!/bin/bash
APP_HOME="/home/ec2-user/app"
PID_FILE="$APP_HOME/app.pid"

echo "--- ApplicationStop Hook ---"
echo "Attempting to stop the application..."

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    echo "Found PID file with PID: $PID"
    if ps -p "$PID" > /dev/null; then # Check if process exists
        echo "Process with PID $PID is running. Killing it..."
        kill -15 "$PID" # Send SIGTERM for graceful shutdown

        # Wait for the process to terminate, with a timeout
        timeout=30 # seconds
        for i in $(seq 1 $timeout); do
            if ! ps -p "$PID" > /dev/null; then
                echo "Process $PID stopped."
                rm -f "$PID_FILE"
                exit 0
            fi
            sleep 1
        done
        echo "Process $PID did not stop gracefully within $timeout seconds. Forcibly killing..."
        kill -9 "$PID" # Send SIGKILL (force kill)
        rm -f "$PID_FILE"
        exit 0
    else
        echo "No process found with PID $PID. Cleaning up PID file."
        rm -f "$PID_FILE"
    fi
else
    echo "PID file ($PID_FILE) not found. Application might not be running or previously stopped. verifying with ps -ef grep now"
    PID=$(pgrep -f 'java -jar')

    if [ -n "$PID" ]; then
      echo "Forcibly killing process $PID"
      kill -9 $PID # Send SIGKILL (force kill)
    else
      echo "No Java process found"
    fi
fi

echo "ApplicationStop Hook completed."
exit 0 # Always exit 0 to allow the deployment to proceed, even if app wasn't running

