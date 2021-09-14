#!/bin/bash

#Use from main directory (opening-black-box)
directoryToDelete=${1}
if [ -z "$directoryToDelete" ]
then
      echo "Directory name to delete is empty"
else
      cd scripts

      exec < ./../monitor-manager/hosts-info.csv
      read header
      while IFS=, read host_ip container_name; do
         sshpass -f "password.env" ssh -n magisterka@${host_ip} "rm -rf ~/opening-black-box/experiments/${$directoryToDelete} ;"
      done
fi
