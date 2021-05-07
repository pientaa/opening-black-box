#!/bin/bash
#Use from main directory (opening-black-box)
path=$(pwd)
filename="experiments_data.zip"
rm -rf experiments_data
mkdir experiments_data
cd scripts

exec < ./../monitor-manager/hosts-info.csv
read header
while IFS=, read host_ip container_name; do
  node="node_${host_ip:(-2)}"
  node_filename="${host_ip:(-2)}_${filename}"

  cd ../experiments_data
  mkdir ${node}
  cd ${path}/scripts

  sshpass -f "password.env" ssh -n magisterka@${host_ip} "cd ~/opening-black-box/experiments/ ; rm -f *.zip ; zip -r -D ${node_filename} . ;"
  echo "${host_ip} data zipped"
  sshpass -f "password.env" scp magisterka@${host_ip}:~/opening-black-box/experiments/${node_filename} ${path}/experiments_data/${node}
  echo "${host_ip} data copied"

  cd ../experiments_data
  cd ${node}
  unzip "*.zip"
  rm -f *.zip
  cd ${path}/scripts
done

head -n 1 password.env | sshpass -f "password.env" ssh -tt 20 'sudo docker exec -u postgres postgres psql -d black-box -c "COPY stage_metrics TO STDOUT WITH CSV HEADER " > stage_metrics.csv ;'
head -n 1 password.env | sshpass -f "password.env" ssh -tt 20 'sudo docker exec -u postgres postgres psql -d black-box -c "COPY task_metrics TO STDOUT WITH CSV HEADER " > task_metrics.csv ;'
sshpass -f "password.env" scp magisterka@192.168.55.20:~/stage_metrics.csv ${path}/experiments_data/stage_metrics.csv
sshpass -f "password.env" scp magisterka@192.168.55.20:~/task_metrics.csv ${path}/experiments_data/task_metrics.csv
sshpass -f "password.env" ssh -n magisterka@192.168.55.20 "rm -f *.csv ;"
