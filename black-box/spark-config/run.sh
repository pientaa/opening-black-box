#!/bin/bash
set -e

docker build -t pienta/spark-base:2.4.7 ./docker/base
echo -e "BUILDED BASE"
docker build -t pienta/spark-master:2.4.7 ./docker/spark-master
echo -e "BUILDED MASTER"
docker build -t pienta/spark-worker:2.4.7 ./docker/spark-worker
echo -e "BUILDED WORKER"
docker build -t pienta/spark-submit:2.4.7 ./docker/spark-submit
echo -e "BUILDED SUBMIT"
docker build -t pienta/black-box-db ./../database

docker-compose up -d

cd ..
docker-compose up -d postgres