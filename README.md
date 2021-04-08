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

### Submit jar to the cluster with script

```bash
scripts/sumbit.sh <function_name>
```

Expected output:
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

### Submit jar to the cluster via REST API
```bash
curl --location --request POST '192.168.55.20:5000/submit' \
--header 'Content-Type: application/json' \
--data-raw '{
    "function_name": "averageTemperatureByDeviceIdSeason"
}'
```

Expected response:
```json
{
    "action": "CreateSubmissionResponse",
    "message": "Driver successfully submitted as driver-20210407145229-0000",
    "serverSparkVersion": "2.4.7",
    "submissionId": "driver-20210407145229-0000",
    "success": true
}
```

### Get the driver status via REST API
```bash
curl --location --request GET '192.168.55.20:5000/status' \
--header 'Content-Type: application/json' \
--data-raw '{
    "driver_id": "driver-20210407145229-0000"
}'
```

Expected response:
```json
{
    "action": "SubmissionStatusResponse",
    "driverState": "FINISHED",
    "serverSparkVersion": "2.4.7",
    "submissionId": "driver-20210407145229-0000",
    "success": true,
    "workerHostPort": "10.5.0.6:40829",
    "workerId": "worker-20210407145657-10.5.0.6-40829"
}
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

## Run experiments

Make sure you have _hosts_info.csv_ file in working directory.

### Create experiments plan csv file
| function_name              | table_name   |
|:--------------------------:| ------------:|
| average  | input_100  | 
| filter   | input_1000 |

### Run manager

```python
python monitor-manager/monitor-manager.py
```