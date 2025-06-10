#!/bin/bash
echo "Starting the MoneyGrab Operations application..."
cd /home/ec2-user/app
nohup java -jar moneychanger-operations-1.0.0-SNAPSHOT.jar > moenygrab.log 2>&1 &
