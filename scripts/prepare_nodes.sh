#!/bin/bash
branch=${1:-main}

all_nodes=(192.168.55.20 192.168.55.11 192.168.55.12 192.168.55.13 192.168.55.14 192.168.55.15 192.168.55.16 192.168.55.17 192.168.55.18 192.168.55.19)

path=$(pwd)
cd black-box
sbt 'set test in Test := {}' clean assembly
cd $path
cd scripts

for i in "${!all_nodes[@]}"
do
  echo ${all_nodes[$i]}
  sshpass -f "password.env" ssh magisterka@${all_nodes[$i]} "cd opening-black-box;git reset --hard; git checkout -f $branch; git fetch && git pull;"
  sshpass -f "password.env" scp ./../black-box/target/scala-2.12/black-box-assembly-1.0.jar magisterka@${all_nodes[$i]}:~/opening-black-box/black-box/target/scala-2.12/black-box-assembly-1.0.jar
  head -n 1 password.env | sshpass -f "password.env" ssh -tt magisterka@${all_nodes[$i]} "sudo cp ~/opening-black-box/black-box/target/scala-2.12/black-box-assembly-1.0.jar ~/opening-black-box/spark-config/mnt/spark-apps/black-box-assembly-1.0.jar;"
done
