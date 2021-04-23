#!/bin/bash

cd scripts

echo 'Init docker swarm, create spark-network, run spark master and set up database'
sshpass -f "password.env" ssh 20 "docker swarm init;"
sshpass -f "password.env" ssh 20 "docker network create -d overlay --attachable --ipam-driver=default --subnet=10.5.0.0/16 spark-network;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/spark-config/spark-master.yml up -d;"
sshpass -f "password.env" ssh 20 "~/opening-black-box/database/get_data.sh;"
sshpass -f "password.env" ssh 20 "docker-compose -f  ~/opening-black-box/database/docker-compose.yml up -d;"
sshpass -f "password.env" ssh 20 "~/miniconda3/bin/conda env update --name system-monitor -f ~/opening-black-box/system-monitor/system-monitor-env.yml;"
nohup sshpass -f "password.env" ssh 20 "~/miniconda3/envs/system-monitor/bin/python3 ~/opening-black-box/system-monitor/system-monitor.py ;" &
nohup sshpass -f "password.env" ssh 20 "~/miniconda3/envs/system-monitor/bin/python3 ~/opening-black-box/monitor-manager/monitor-manager.py ;" &
sshpass -f "password.env" scp ./../tpc-ds-tool.zip magisterka@$192.168.55.20:~/opening-black-box
head -n 1 password.env | sshpass -f "password.env" ssh -tt 20 "sudo apt install gcc make flex bison"
sshpass -f "password.env" ssh 20 "~/opening-black-box/database/prepare_tcp_ds.sh" &