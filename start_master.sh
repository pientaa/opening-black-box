#!/bin/bash

echo 'Create spark-network, run spark master and set up database'
ssh magisterka@192.168.55.20 "docker network create -d overlay --attachable --ipam-driver=default --subnet=10.5.0.0/16 spark-network; docker-compose -f  ~/opening-black-box/spark-config/spark-master.yml up -d; ~/opening-black-box/database/get_data.sh; docker-compose -f  ~/opening-black-box/database/docker-compose.yml up -d"