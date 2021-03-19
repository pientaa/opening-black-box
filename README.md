# Opening a black-box


## Run cluster remotely

In order to configure spark cluster remotely, run on master node (192.168.55.20)
```
docker swarm init
```
and join docker swarm on other nodes. Then use following scripts to run `spark-master` and `n` number of `spark-worker`s.
```
start_master.sh
start_workers.sh
```