#!/bin/bash
functionName=${1:-averageTemperatureByDeviceIdSeason}
docker exec spark-master curl -X POST http://10.5.0.2:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
  "appResource": "/opt/spark-apps/black-box-assembly-1.0.jar",
  "sparkProperties": {
    "spark.master": "spark://spark-master:7077",
    "spark.driver.memory": "4g",
    "spark.driver.cores": "2",
    "spark.app.name": "BlackBox",
    "spark.submit.deployMode": "cluster",
    "spark.driver.supervise": "true"
  },
  "clientSparkVersion": "2.4.7",
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