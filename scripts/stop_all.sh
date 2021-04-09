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
    echo "Select number of workers to stop: (default 3)"
    read num_of_workers
    num_of_workers="${num_of_workers:=3}"
}

function stop_master() {
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "docker rm -f postgres;"
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "docker rm -f gateway;"
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "docker rm -f spark-master;"
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "docker network prune --force;"
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "docker swarm leave --force;"
      sshpass -f "password.env" ssh magisterka@192.168.55.20 "ps aux | grep system-monitor.py | head -n 1 | awk '{print $2}' | xargs kill ;"
}

function stop_workers() {
    for i in "${!available_workers[@]}"
    do
      index=$(($i +1))
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} "docker rm -f spark-worker-${index};"
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} "docker network prune --force;"
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} "docker swarm leave --force;"
#      There is a problem with awk in this line - actually it does the job (kills the process) but somehow awk doesn't work
      sshpass -f "password.env" ssh magisterka@${available_workers[$i]} 'kill $(ps aux | grep system-monitor.py | head -n 1 | awk '"'{print $2}'"') ;'
    done
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
    done
}

select_node_to_start
select_number_of_workers
trim_worker_nodes_list
echo 'Master node: ' ${master_node}
echo 'Available workers: ' ${available_workers[@]}
echo 'All nodes: '${all_nodes[@]}

stop_workers
stop_master
