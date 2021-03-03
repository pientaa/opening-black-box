#!/bin/bash
set -e
path=$(pwd)

echo "Removing all existing docker containers ..."
result=$(docker ps -aq)

if [[ -n "$result" ]]; then
  docker rm -f $(docker ps -aq)
else
  echo "No containers found ..."
fi

cd $path
docker rmi -f pienta/spark-base:2.4.7 pienta/spark-master:2.4.7 pienta/spark-worker:2.4.7 pienta/black-box-db

docker build -t pienta/spark-base:2.4.7 ./docker/base
docker build -t pienta/spark-master:2.4.7 ./docker/spark-master
docker build -t pienta/spark-worker:2.4.7 ./docker/spark-worker
docker build -t pienta/black-box-db ./../database