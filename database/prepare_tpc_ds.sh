#!/bin/bash

sshpass -f "./scripts/password.env" scp ./../tpc-ds-tool.zip magisterka@$192.168.55.20:~/opening-black-box
sshpass -f "./scripts/password.env" ssh 20 "unzip ~/opening-black-box/tpc-ds-tool.zip -d ~/opening-black-box/database/tpc-ds;"
sshpass -f "./scripts/password.env" ssh 20 "mkdir ~/opening-black-box/database/tpc-data;"
sshpass -f "./scripts/password.env" ssh 20 "cd ~/opening-black-box/database/tpc-ds/v3.0.0/tools; copy tpcds.sql ./../../../ ; make ;"
