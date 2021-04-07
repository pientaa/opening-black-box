#!/bin/bash

cd scripts

all_nodes=(192.168.55.11 192.168.55.12 192.168.55.13 192.168.55.14 192.168.55.15 192.168.55.16 192.168.55.17 192.168.55.18 192.168.55.19)

function select_node_to_start() {
    master_node=192.168.55.20
    echo "Select the node to start from (default: 192.168.55.11):
    1 - 192.168.55.11
    2 - 192.168.55.12
    3 - 192.168.55.13
    4 - 192.168.55.14
    5 - 192.168.55.15
    6 - 192.168.55.16
    7 - 192.168.55.17
    8 - 192.168.55.18
    9 - 192.168.55.19"
    read node_to_start_index
    case $node_to_start_index in
        1) node_to_start=192.168.55.11 ;;
        2) node_to_start=192.168.55.12 ;;
        3) node_to_start=192.168.55.13 ;;
        4) node_to_start=192.168.55.14 ;;
        5) node_to_start=192.168.55.15 ;;
        6) node_to_start=192.168.55.16 ;;
        7) node_to_start=192.168.55.17 ;;
        8) node_to_start=192.168.55.18 ;;
        9) node_to_start=192.168.55.19 ;;
        *) node_to_start=192.168.55.11 ;;
    esac
    node_to_start_index=$(($node_to_start_index - 1))
}

function select_number_of_workers() {
    echo "Select number of workers: (default 3)"
    read num_of_workers
    num_of_workers="${num_of_workers:=3}"
}

function create_worker_yml() {
#  master: 10.5.0.2, worker-1: 10.5.0.4, ...
  last_ip_index=$(($1 * 2 + 2))

  filename=spark-worker-${1}.yml
  if [ ! -f $filename ]
  then
    echo 'Creating file: ' $filename
    touch $filename
  fi

  echo $'version: "3" \nservices:\n  'spark-worker-${1}:\
  $'\n    image: pienta/spark-worker:2.4.7'\
  $'\n    container_name: spark-worker-'${1}\
  $'\n    hostname: 10.5.0.'${last_ip_index}\
  $'\n    ports:'\
  $'\n      - "8080:8081"'\
  $'\n    env_file: env/spark-worker.sh'\
  $'\n    environment:'\
  $'\n      - "SPARK_LOCAL_IP=spark-worker-'${1}'"'\
  $'\n    volumes:'\
  $'\n      - ./mnt/spark-apps:/opt/spark-apps'\
  $'\n      - ./mnt/spark-data:/opt/spark-data' > $filename
}

function trim_worker_nodes_list() {
    all_nodes_size=${#all_nodes[@]}
    n=$((all_nodes_size - node_to_start_index))
    if [[ $n -gt $num_of_workers ]]; then
      n=$((${num_of_workers}))
    fi

    available_workers=()
    for i in "${all_nodes[@]:${node_to_start_index}:${n}}"
    do
      available_workers+=($i)
      echo $i
    done
}

function prepare_composes() {
    for i in "${!available_workers[@]}"
    do
      num_of_node=$(($i + 1))
      filename=spark-worker-${num_of_node}.yml
      create_worker_yml ${num_of_node}
      echo ${available_workers[$i]}
      sshpass -f "password.env" scp $filename magisterka@${available_workers[$i]}:~/opening-black-box/spark-config/${filename}
    done
}

function run_containers() {
    for i in "${!available_workers[@]}"
    do
      index=$(($i +1))
      echo $index
      last_ip_index=$(($index * 2 + 2))
      ip_addr='10.5.0.'${last_ip_index}
      echo $ip_addr
      echo ${available_workers[$i]}
      token=$(sshpass -f "password.env" ssh 20 "docker swarm join-token -q worker;")
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} "docker swarm join --token ${token} 192.168.55.20:2377"
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} "docker-compose -f ~/opening-black-box/spark-config/spark-worker-${index}.yml up -d; docker network connect --ip ${ip_addr} spark-network spark-worker-${index}"
    done
}

initial_path=$(pwd)
echo $initial_path

select_node_to_start
select_number_of_workers
trim_worker_nodes_list
echo 'Master node: ' ${master_node}
echo 'Available workers: ' ${available_workers[@]}
echo 'All nodes: '${all_nodes[@]}

prepare_composes
run_containers