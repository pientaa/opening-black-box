#!/bin/bash

docker-compose -f local.yml up -d

sleep 5

docker network connect --ip 10.5.0.8 sparkconfig_spark-network postgres