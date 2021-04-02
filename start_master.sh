#!/bin/bash

echo 'Init docker swarm, create spark-network, run spark master and set up database'
sshpass -f "password.env" ssh 20 "docker swarm init;"
sshpass -f "password.env" ssh 20 "docker network create -d overlay --attachable --ipam-driver=default --subnet=10.5.0.0/16 spark-network;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/spark-config/spark-master.yml up -d;"
sshpass -f "password.env" ssh 20 "~/opening-black-box/database/get_data.sh;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/database/docker-compose.yml up -d;"