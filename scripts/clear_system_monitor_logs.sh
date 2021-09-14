#!/bin/bash

#Use from main directory (opening-black-box)
cd scripts

exec < ./../monitor-manager/hosts-info.csv
read header
while IFS=, read host_ip container_name; do
   sshpass -f "password.env" ssh -n magisterka@${host_ip} "rm ~/opening-black-box/system-monitor.log ;"
   echo "${host_ip} logs cleared"
done