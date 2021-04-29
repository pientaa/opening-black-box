#!/bin/bash

data_size=${1:-1}
directory_name="${data_size}GB"

sshpass -f "./scripts/password.env" ssh 20 "cd ~/opening-black-box/database/tpc-data ; mkdir $directory_name "
sshpass -f "./scripts/password.env" ssh 20 "cd ~/opening-black-box/database/tpc-ds/v3.0.0/tools ; ./dsdgen -SCALE $data_size -DIR ./../../../tpc-data/$directory_name -TERMINATE N"