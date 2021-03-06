#!/bin/bash

cd scripts

echo 'Init docker swarm, create spark-network, run spark master and set up database'
sshpass -f "password.env" ssh 20 "docker swarm init;"
sshpass -f "password.env" ssh 20 "docker network create -d overlay --attachable --ipam-driver=default --subnet=10.5.0.0/16 spark-network;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/spark-config/spark-master.yml up -d;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/database/docker-compose.yml up -d;"
echo 'Run system-monitor'
sshpass -f "password.env" ssh 20 "~/miniconda3/bin/conda env update --name system-monitor -f ~/opening-black-box/system-monitor/system-monitor-env.yml;"
nohup sshpass -f "password.env" ssh 20 "~/miniconda3/envs/system-monitor/bin/python3 ~/opening-black-box/system-monitor/system-monitor.py ;" &
echo 'Run monitor-manager'
nohup sshpass -f "password.env" ssh 20 "~/miniconda3/envs/system-monitor/bin/python3 ~/opening-black-box/monitor-manager/monitor-manager.py ;" &

