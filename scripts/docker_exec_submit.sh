#!/bin/bash
functionName=${1:-averageTemperatureByDeviceIdSeason}

#curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://10.5.0.2:6066/api/v1/applications
#
#http://10.5.0.4:8081/logPage/?driverId=driver-20210720173300-0001&logType=stderr
#http://10.5.0.9:8081/logPage/?driverId=driver-20210720170656-0000&logType=stderr
#
#http://10.5.0.3:8081/logPage/?appId=app-20210720171629-0000&executorId=0&logType=stderr
#http://10.5.0.3:8081/logPage/?appId=app-20210720171629-0000&executorId=0&logType=stderr
docker exec spark-master curl -X POST http://10.5.0.2:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
  "appResource": "/opt/spark-apps/black-box-assembly-1.0.jar",
  "sparkProperties": {
    "spark.master": "spark://spark-master:7077",
    "spark.driver.memory": "8g",
    "spark.driver.cores": "2",
    "spark.app.name": "BlackBox",
    "spark.submit.deployMode": "cluster",
    "spark.driver.supervise": "true"
  },
  "clientSparkVersion": "3.0.2",
  "mainClass": "BlackBox",
  "environmentVariables": {
    "SPARK_ENV_LOADED": "1"
  },
  "action": "CreateSubmissionRequest",
  "appArgs": [
    "postgres",
    "'$functionName'"
  ]
}'