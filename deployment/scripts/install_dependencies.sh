#!/bin/bash
# scripts/install_dependencies.sh
# This script installs any system-level dependencies required by the application.
# It runs BEFORE the application files are copied to /home/ec2-user/app.

echo "--- BeforeInstall Hook ---"
echo "Running system-level installation tasks..."

# Example: Update package lists and install Java if not already done via userdata or AMI
# Ensure Java 21 is installed. Assuming a system where 'java' might not be pre-installed or is an older version.
# For Amazon Linux:
sudo yum update -y
sudo yum install -y java-21-amazon-corretto-devel

# Example: Install other necessary packages (e.g., unzip, git, etc.)
sudo yum install -y unzip # For Amazon Linux

# Create necessary directories if they don't exist
# Note: These are directories *outside* the app's final destination, or temporary ones.
# The app's /home/ec2-user/app directory is handled by CodeDeploy's file deployment.
# mkdir -p /var/log/my-java-app # Example for external logs if needed

echo "BeforeInstall Hook completed."
exit 0 # Exit 0 for success