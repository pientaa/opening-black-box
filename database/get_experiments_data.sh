#!/bin/bash

#Use from main directory (opening-black-box)
path=$(pwd)
timestamp=$(date "+%Y_%m_%d_%H_%M")
filename="experiments_data_${timestamp}.zip"

mkdir experiments_data
cd scripts

sshpass -f "password.env" ssh 20 "cd ~/opening-black-box/experiments/ ; zip -r -D ${filename} . ; mv ./${filename} ./../../"
sshpass -f "password.env" scp magisterka@192.168.55.20:~/${filename} ${path}/experiments_data
sshpass -f "password.env" ssh 20 "rm ${filename} ; rm -rf ~/opening-black-box/experiments/* ;"