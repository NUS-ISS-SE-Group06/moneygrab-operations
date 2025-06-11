#!/bin/bash
# scripts/start_application.sh
# This script starts the Java application.

APP_HOME="/home/ec2-user/app"
JAR_NAME="moneychanger-operations-1.0.0-SNAPSHOT.jar" # IMPORTANT: Use the actual JAR name here
LOG_FILE="$APP_HOME/log/moneygrab_operations_startup.log"
PID_FILE="$APP_HOME/app.pid"
SPRING_PROFILE="prd"

echo "--- ApplicationStart Hook ---"
echo "Starting the application..."

# Ensure the build directory exists
mkdir -p "$APP_HOME/build"

# Start the Java application in the background
# Redirect stdout and stderr to a log file
# Use nohup to ensure the process continues running after the shell exits
# Store the PID in a file for later stopping
nohup java -jar "$APP_HOME/build/$JAR_NAME" --spring.profiles.active="$SPRING_PROFILE" > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE" # Save the PID of the last background process

# Give the app a moment to start (adjust as needed)
sleep 5

echo "Application started. Check logs at $LOG_FILE"
echo "PID stored in $PID_FILE"
echo "ApplicationStart Hook completed."

# Important: Exit 0 unless a critical failure happened during startup
# Validation will happen in the ValidateService hook
exit 0 # Exit 0
