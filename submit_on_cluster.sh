#!/bin/bash
functionName=${1:-averageTemperatureByDeviceIdSeason}
# Get driver status
#docker exec -it spark-master curl http://10.5.0.2:6066/v1/submissions/status/<driver-id>

sshpass -f "password.env" ssh 20 "~/opening-black-box/submit.sh"