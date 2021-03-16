#!/bin/bash

docker-compose up -d

sleep 5

docker network connect --ip 10.5.0.8 spark-network postgres