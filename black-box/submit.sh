#!/bin/bash
echo -e "Building black-box ..."
sbt assembly

sudo cp ./target/scala-2.11/black-box-assembly-1.0.jar ./../spark-config/mnt/spark-apps/black-box-assembly-1.0.jar

#ssh magisterka@192.168.55.15
curl -vX POST http://localhost:6066/v1/submissions/create -d @submit.json \
 --header "Content-Type: application/json"