#!/bin/bash

docker rmi pienta/gateway:1.0.0
docker build -t pienta/gateway:1.0.0 .