#!/bin/bash
#TODO: Configure ths script to run cluster on production environment
#TODO: Should take parameters like number of workers and set up cluster on available nodes

echo "Removing all existing docker containers ..."
result=$(docker ps -aq)

if [[ -n "$result" ]]; then
  docker rm -f $(docker ps -aq)
else
  echo "No containers found ..."
fi

docker-compose up -d