#!/bin/bash
# scripts/after_install.sh
# This script performs tasks after the application files are copied, but before the app starts.

APP_HOME="/home/ec2-user/app"

echo "--- AfterInstall Hook ---"
echo "Running post-installation tasks..."

# Example: Run database migrations (Flyway, Liquibase, etc.)
# cd "$APP_HOME/build"
# java -jar flyway.jar migrate # Example command, actual command depends on your setup

# Example: Copy environment-specific configuration files
# cp "$APP_HOME/config/production.properties" "$APP_HOME/application.properties"

echo "AfterInstall Hook completed."
exit 0