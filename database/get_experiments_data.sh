#!/bin/bash

#Use from main directory (opening-black-box)
path=$(pwd)
timestamp=$(date "+%Y_%m_%d_%H_%M")
filename="experiments_data_${timestamp}.zip"

mkdir experiments_data
cd scripts

exec < ./../monitor-manager/hosts-info.csv
read header
while IFS=, read host_ip container_name; do
  node_filename="${host_ip:(-2)}_${filename}"
  sshpass -f "password.env" ssh magisterka@${host_ip} "cd ~/opening-black-box/experiments/ ; zip -r -D ${node_filename} . ; mv ./${node_filename} ./../../"
  sshpass -f "password.env" scp magisterka@${host_ip}:~/${node_filename} ${path}/experiments_data
  sshpass -f "password.env" ssh magisterka@${host_ip} "rm ${node_filename} ; rm -rf ~/opening-black-box/experiments/* ;"
done