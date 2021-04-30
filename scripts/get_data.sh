#!/bin/bash
# Use from main directory (opening-black-box)
path=$(pwd)

sudo rm -rf database/tpc-data

cd scripts

sshpass -f "password.env" scp -r magisterka@192.168.55.20:/home/magisterka/opening-black-box/database/tpc-data ${path}/database