#!/bin/bash
functionName=${1:-averageTemperatureByDeviceIdSeason}
sshpass -f "password.env" ssh 20 "~/opening-black-box/submit.sh $functionName;"