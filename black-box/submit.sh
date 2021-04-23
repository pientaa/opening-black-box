#!/bin/bash
function=${1:-dayOfWeek}
echo $host
echo $function

echo -e "Building black-box ..."
sbt 'set test in Test := {}' clean assembly

sudo cp ./target/scala-2.12/black-box-assembly-1.0.jar ./../spark-config/mnt/spark-apps/black-box-assembly-1.0.jar

# Get driver status
#docker exec -it spark-master curl http://10.5.0.2:6066/v1/submissions/status/<driver-id>

docker exec -it spark-master curl -X POST http://10.5.0.2:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
  "appResource": "/opt/spark-apps/black-box-assembly-1.0.jar",
  "sparkProperties": {
    "spark.master": "spark://spark-master:7077",
    "spark.driver.memory": "4g",
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
    "'$function'"
  ]
}'