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
docker rmi -f pienta/spark-base:3.0.2 pienta/spark-master:3.0.2 pienta/spark-worker:3.0.2 pienta/black-box-db

docker build -t pienta/spark-base:3.0.2 ./docker/base
docker build -t pienta/spark-master:3.0.2 ./docker/spark-master
docker build -t pienta/spark-worker:3.0.2 ./docker/spark-worker
docker build -t pienta/black-box-db ./../database