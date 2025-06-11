#!/bin/bash
# scripts/start_application.sh
# This script starts the Java application.

APP_HOME="/home/ec2-user/app"
JAR_NAME="moneychanger-operations-1.0.0-SNAPSHOT.jar" # IMPORTANT: Use the actual JAR name here
LOG_FILE="$APP_HOME/log/moneygrab_operations_startup.log"
PID_FILE="$APP_HOME/app.pid"
SPRING_PROFILE="prd"
AWS_REGION="ap-southeast-1"

echo "--- ApplicationStart Hook ---"
echo "Starting the application..."

# Ensure the build directory exists
mkdir -p "$APP_HOME/build"
mkdir -p "$APP_HOME/log"

# --- Fetch Secrets from AWS Systems Manager Parameter Store ---
echo "Fetching database credentials from AWS Systems Manager Parameter Store..."

# Fetch DB_URL
DB_URL_PARAM_NAME="/moneygrab/prd/DB_URL" # Adjust parameter path as needed
DB_URL=$(aws ssm get-parameter --name "$DB_URL_PARAM_NAME" --with-decryption --query "Parameter.Value" --output text --region "$AWS_REGION")
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to retrieve DB_URL from SSM Parameter Store." >&2
    exit 1
fi
echo "DB_URL fetched successfully."

# Fetch DB_USERNAME
DB_USERNAME_PARAM_NAME="/moneygrab/prd/DB_USERNAME" # Adjust parameter path as needed
DB_USERNAME=$(aws ssm get-parameter --name "$DB_USERNAME_PARAM_NAME" --with-decryption --query "Parameter.Value" --output text --region "$AWS_REGION")
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to retrieve DB_USERNAME from SSM Parameter Store." >&2
    exit 1
fi
echo "DB_USERNAME fetched successfully."

# Fetch DB_PASSWORD
DB_PASSWORD_PARAM_NAME="/moneygrab/prd/DB_PASSWORD" # Adjust parameter path as needed
DB_PASSWORD=$(aws ssm get-parameter --name "$DB_PASSWORD_PARAM_NAME" --with-decryption --query "Parameter.Value" --output text --region "$AWS_REGION")
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to retrieve DB_PASSWORD from SSM Parameter Store." >&2
    exit 1
fi
echo "DB_PASSWORD fetched successfully."

# --- Export secrets as environment variables for the Java process ---
export DB_URL
export DB_USERNAME
export DB_PASSWORD

# Start the Java application in the background
# Redirect stdout and stderr to a log file
# Use nohup to ensure the process continues running after the shell exits
# Store the PID in a file for later stopping
echo "Executing: nohup java -jar \"$APP_HOME/build/$JAR_NAME\" --spring.profiles.active=\"$SPRING_PROFILE\" > \"$LOG_FILE\" 2>&1 &"
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
