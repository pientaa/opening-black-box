#!/bin/bash

all_nodes=(192.168.55.11 192.168.55.12 192.168.55.13 192.168.55.14 192.168.55.15 192.168.55.16 192.168.55.17 192.168.55.18 192.168.55.19)

function select_master() {
    echo "Select the master node (default: local mode):
    0 - 192.168.55.11
    1 - 192.168.55.12
    2 - 192.168.55.13
    3 - 192.168.55.14
    4 - 192.168.55.15
    5 - 192.168.55.16
    6 - 192.168.55.17
    7 - 192.168.55.18
    8 - 192.168.55.19"
    read master_node_index
    case $master_node_index in
        0) master_node=192.168.55.11 ;;
        1) master_node=192.168.55.12 ;;
        2) master_node=192.168.55.13 ;;
        3) master_node=192.168.55.14 ;;
        4) master_node=192.168.55.15 ;;
        5) master_node=192.168.55.16 ;;
        6) master_node=192.168.55.17 ;;
        7) master_node=192.168.55.18 ;;
        8) master_node=192.168.55.19 ;;
        *) # local mode - don't use on prod
           master_node=$(hostname -I | awk '{print $1}')
           master_node_index=0
           all_nodes=("${master_node}")
          ;;
    esac
}

function select_number_of_workers() {
    echo "Select number of workers: (default 3)"
    read num_of_workers
    num_of_workers="${num_of_workers:=3}"
}

function trim_all_nodes() {
    all_nodes_size=${#all_nodes[@]}
    n=$((all_nodes_size - master_node_index))
    if [[ $n -gt $num_of_workers ]]; then
      n=$((${num_of_workers} + 1))
    fi
    available_nodes=${all_nodes[@]:${master_node_index}:${n}}
    num_of_available_workers=$(( ${#available_nodes[@]} - 1))
    echo ${num_of_available_workers}
}

select_master
select_number_of_workers
trim_all_nodes
echo $master_node_index
echo ${all_nodes["${master_node_index}"]}
echo ${available_nodes[@]}
echo ${all_nodes[@]}
