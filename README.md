# Opening a black-box


## Run cluster remotely

### Configure ssh connection

It's recommended to use aliases for connection to cluster.
Modify `~/.ssh/config` following the pattern:

```bash
Host <number_of_node>
  Port 22
  User magisterka
  HostName <node_ip_address>
```

Install `sshpass` and configure aliases for connection with the password:
```bash
alias magisterka='sshpass -p<password>'
```

### Create env file with password
```bash
touch password.env
echo <your_password> > password.env
```

### Configure docker swarm

Then you can connect to each node by simple
```bash
magisterka ssh <number_of_node>
```

In order to configure spark cluster remotely, run on master node (192.168.55.20)
```bash
docker swarm init
```
and join docker swarm on other nodes.

### Run cluster

Then use following scripts to run `spark-master` and `n` number of `spark-worker`s.
```bash
start_master.sh
start_workers.sh
```

### Potential problems

If you get error like
```
Error response from daemon: attaching to network failed, make sure your network options are correct and check manager logs: context deadline exceeded
```
Inspect docker network (`spark-network`) on the master node and make sure that it took addresses
- 10.5.0.2
- 10.5.0.3
