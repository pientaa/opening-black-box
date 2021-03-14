# Spark config

## Cluster configuration

### Configuration tested on nodes:

- `192.168.55.15` (master)
- `192.168.55.16` (worker)
- `192.168.55.17` (worker)

1. On master node:

```
cd ~/opening-black-box/spark-config
docker swarm init
docker network create -d overlay --attachable --ipam-driver=default --subnet=10.5.0.0/16 spark-network
docker network create -d bridge spark-network-bridge
docker network connect spark-network-bridge spark-master
docker-compose -f spark-master.yml up -d
```

2. On each worker node join docker swarm
3. Make sure nodes are connected (execute on master)

```
docker node ls
```

4. On worker 1

```
cd ~/opening-black-box/spark-config
docker-compose -f spark-worker-1.yml up -d
docker network connect --ip 10.5.0.4 spark-network spark-worker-1 
```

Make sure worker connected to master

```
docker logs -f spark-worker-1 --tail 100
```

5. On worker 2

```
cd ~/opening-black-box/spark-config
docker-compose -f spark-worker-2.yml up -d
docker network connect --ip 10.5.0.6 spark-network spark-worker-2
```

6. Clean up cluster

- on worker 1

```
docker rm -f $(docker ps -aq)
docker network rm sparkconfig_default
docker swarm leave --force
```

- on worker 2

```
docker rm -f $(docker ps -aq)
docker network rm sparkconfig_default 
docker swarm leave --force
```

- on master

```
docker rm -f $(docker ps -aq)
docker network rm spark-network
docker swarm leave --force
```

### Proof that it actually works:

```
$ docker exec -it spark-master /bin/bash
bash-4.3# ping 10.5.0.3
PING 10.5.0.3 (10.5.0.3): 56 data bytes
64 bytes from 10.5.0.3: seq=0 ttl=64 time=0.380 ms
64 bytes from 10.5.0.3: seq=1 ttl=64 time=0.324 ms
64 bytes from 10.5.0.3: seq=2 ttl=64 time=0.315 ms
^C
--- 10.5.0.3 ping statistics ---
3 packets transmitted, 3 packets received, 0% packet loss
round-trip min/avg/max = 0.315/0.339/0.380 ms
bash-4.3# ping 10.5.0.4
PING 10.5.0.4 (10.5.0.4): 56 data bytes
64 bytes from 10.5.0.4: seq=0 ttl=64 time=0.397 ms
64 bytes from 10.5.0.4: seq=1 ttl=64 time=0.421 ms
64 bytes from 10.5.0.4: seq=2 ttl=64 time=0.334 ms
^C
--- 10.5.0.4 ping statistics ---
3 packets transmitted, 3 packets received, 0% packet loss
round-trip min/avg/max = 0.334/0.384/0.421 ms
bash-4.3# 
```

## Run black box

### Prerequisites

- `192.168.55.15` (master), spark-network: `10.5.0.2`
- `192.168.55.16` (worker), spark-network: `10.5.0.4`
- `192.168.55.17` (worker), spark-network: `10.5.0.6`
- `192.168.55.18` (database), spark-network: `10.5.0.8`
- master and every worker need to have the same jar built in `spark-config/mnt/spark-apps/` folder

For some reasons `blackbox/submit.sh` doesn't work. Workaround to run experiment (on master node):

```
docker exec -it spark-master /bin/bash
curl -X POST http://10.5.0.2:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{ 
  "appResource": "/opt/spark-apps/black-box-assembly-1.0.jar",
  "sparkProperties": {
    "spark.master": "spark://spark-master:7077",
    "spark.driver.memory": "4g",
    "spark.driver.cores": "2",
    "spark.app.name": "BlackBox",
    "spark.submit.deployMode": "cluster",
    "spark.driver.supervise": "true"
  },
  "clientSparkVersion": "2.4.7",
  "mainClass": "BlackBox",
  "environmentVariables": {
    "SPARK_ENV_LOADED": "1"
  },
  "action": "CreateSubmissionRequest",
  "appArgs": [
    "10.5.0.8",
    "averageTemperatureByDeviceIdSeason"
  ]
}'
```

Then you can curl for driver state
```
curl http://10.5.0.2:6066/v1/submissions/status/driver-20210314163705-0004
```