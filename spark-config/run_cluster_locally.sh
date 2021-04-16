#!/bin/bash

echo "Removing all existing docker containers ..."
result=$(docker ps -aq)

if [[ -n "$result" ]]; then
  docker rm -f $(docker ps -aq)
else
  echo "No containers found ..."
fi

docker-compose -p sparkconfig up -d

cd ../database
./run_database_locally.sh