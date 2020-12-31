#!/bin/bash
path=$(pwd)
prometheus_path='./prometheus'
secs=10

echo "Removing all existing docker containers ..."
result=$(docker ps -aq)

if [[ -n "$result" ]]; then
  docker rm -f $(docker ps -aq)
else
  echo "No containers found ..."
fi

echo -e "Building black-box ..."
sbt assembly

udf_detector_path='../udf-detector'

cd $udf_detector_path

echo -e "Building experiment-executor ..."
sbt assembly

cd $path

echo -e "Running containers ..."
docker-compose up -d

#cd $prometheus_path
#
#echo -e "Running monitoring ..."
#docker-compose up -d

#TODO: do sth with that
#while [[ "$(docker ps -q -f name=black-box)" ]]; do
#  docker stats black-box --format "{{.ID}}\t{{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}\t{{.BlockIO}}\t{{.PIDs}}" -a --no-stream | ts '[%Y-%m-%d %H:%M:%S]'| tee -a mystats.csv
#done

#cd $path

docker-compose logs -f black-box experiment-executor
