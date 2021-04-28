#!/bin/bash
data_size=${1:-1}

cd ~opening-black-box/database/tpc-ds/v3.0.0/tools
./dsdgen -SCALE $data_szie -DIR ./../../../data