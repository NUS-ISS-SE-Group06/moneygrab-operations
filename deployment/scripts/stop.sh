#!/bin/bash
echo "Stopping existing Java process..."
PID=$(pgrep -f 'java -jar')

if [ -n "$PID" ]; then
  echo "Killing process $PID"
  kill -9 $PID
else
  echo "No Java process found"
fi
