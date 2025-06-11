#!/bin/bash
# scripts/after_install.sh
# This script performs tasks after the application files are copied, but before the app starts.

APP_HOME="/home/ec2-user/app"

echo "--- AfterInstall Hook ---"
echo "Running post-installation tasks..."

# Ensure the app directory exists and set its ownership recursively
# This command changes ownership of /home/ec2-user/app and all its contents
# to 'ec2-user:ec2-user'.
# It's crucial that the CodeDeploy agent (or the 'runas' user of this hook)
# has sudo privileges to execute this command.
sudo chown -R ec2-user:ec2-user "$APP_HOME"
echo "Ownership of $APP_HOME and its contents set to ec2-user."

echo "AfterInstall Hook completed."
exit 0