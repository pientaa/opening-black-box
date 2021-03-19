#!/bin/bash
#Change this directory if want to run locally
filename=~/opening-black-box/database/measurements.csv

if [ ! -f $filename ]
then
  echo 'Getting data to file: ' $filename
  wget https://gitlab.com/pientaa/black-box-data/-/raw/master/measurements.csv
fi
