# Opening a black-box


## Run cluster remotely

### Configure ssh connection

It's recommended to use aliases for connection to cluster. Otherwise, some scripts require modification.
Modify `~/.ssh/config` following the pattern:

```bash
Host <number_of_node>
  Port 22
  User magisterka
  HostName <node_ip_address>
```

### Create env file with password
```bash
touch password.env
echo <your_password> > password.env
```

### Configure and run cluster

```bash
start_master.sh
start_workers.sh
```

### Stop cluster
```bash
stop_all.sh
```

### Potential problems

If you get error like
```
Error response from daemon: attaching to network failed, make sure your network options are correct and check manager logs: context deadline exceeded
```
Inspect docker network (`spark-network`) on the master node and make sure that it took addresses
- 10.5.0.2
- 10.5.0.3
