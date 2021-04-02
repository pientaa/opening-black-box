# Opening a black-box

## Run cluster remotely

### Configure ssh connection

It's recommended to use aliases for connection to cluster. Otherwise, some scripts won't work. Modify `~/.ssh/config`
following the pattern:

```bash
Host <number_of_node>
  Port 22
  User magisterka
  HostName <node_ip_address>
```

### Create env file with password

```bash
cd scripts
touch password.env
echo <your_password> > password.env
```

### Configure and run cluster

```bash
scripts/start_master.sh
scripts/start_workers.sh
```

### Build black box and update cluster nodes

```bash
scripts/prepare_nodes.sh
```

### Submit jar to cluster

```bash
scripts/sumbit.sh <function_name>
```

Expect output:
```
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0{
  "action" : "CreateSubmissionResponse",
  "message" : "Driver successfully submitted as driver-20210402161642-0000",
  "serverSparkVersion" : "2.4.7",
  "submissionId" : "driver-20210402161642-0000",
  "success" : true
100   779  100   223  100   556    888   2215 --:--:-- --:--:-- --:--:--  3103
```

### Stop cluster

```bash
scripts/stop_all.sh
```

### Potential problems

If you get error like

```
Error response from daemon: attaching to network failed, make sure your network options are correct and check manager logs: context deadline exceeded
```

Inspect docker network (`spark-network`) on the master node and make sure that it took addresses

- 10.5.0.2
- 10.5.0.3
