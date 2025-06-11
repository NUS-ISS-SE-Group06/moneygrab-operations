#!/bin/bash
# scripts/validate_service.sh
# This script validates if the application service is healthy.

APP_URL="http://localhost:8688/api/actuator/health"
MAX_RETRIES=10
RETRY_INTERVAL=10 # seconds

echo "--- ValidateService Hook ---"
echo "Performing health checks for application at $APP_URL..."

for i in $(seq 1 $MAX_RETRIES); do
    echo "Attempt $i/$MAX_RETRIES: Checking service health..."
    # Use curl with a short timeout for the connection itself
    HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$APP_URL" --connect-timeout 5)

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo "Service is healthy (HTTP Status: 200)."
        echo "ValidateService Hook completed successfully."
        exit 0 # Success!
    else
        echo "Service not yet healthy (HTTP Status: $HTTP_STATUS). Retrying in $RETRY_INTERVAL seconds..."
        sleep $RETRY_INTERVAL
    fi
done

echo "Service did not become healthy within $MAX_RETRIES attempts."
echo "ValidateService Hook FAILED."
exit 1 # Failure! CodeDeploy will roll back