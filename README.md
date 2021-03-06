# Opening a black-box

## Run cluster locally

1. If first time here -> download data with `getdata.sh` in `database` directory.
2. Go to `spark-config` directory.
4. Run `run_cluster_locally.sh`.
4. Access spark-master UI at http://localhost:8080/
5. Submit jar with `submit.sh` in `black-box` directory.

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
scripts/prepare_nodes.sh <git_branch_to_checkout:-main>
scripts/start_master.sh
scripts/start_workers.sh
```

### Generate TPC-DS data

Make sure that TPC-DS tool is available on master node - if not got
to [`database/README`](https://github.com/pientaa/opening-black-box/tree/tpc-ds/database#setup-tpc-ds).

Parametrize the script with desired data size. Default value is 1 GB. TPC-DS enables to generate data from 1 GB to 10
TB.

```bash
database/generate_tpc_ds.sh <data_size_in_GB>
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
  "serverSparkVersion" : "3.0.2",
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
  "serverSparkVersion": "3.0.2",
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
  "serverSparkVersion": "3.0.2",
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

Make sure you have _hosts_info.csv_ file in `monitor-manager` directory. The file should end with an empty line.

| host_ip              | container_name   |
|:--------------------------:| ------------:|
| 192.168.55.20  | spark-master  |
| 192.168.55.11  | spark-worker-1  |
| 192.168.55.12  | spark-worker-2  |
| 192.168.55.13  | spark-worker-3  |

### Create experiments plan csv file

For example:

| function_name              | dataset_size   | iterations |
|:--------------------------:| ------------:|  ------------:|
| countDistinctTicketNumber  | 1GB  | 25 |

### Check if monitor-manager is running

```bash
curl --location --request GET 'http://192.168.55.20:8888/'
```

### Start experiments

```bash
curl --location --request POST 'http://192.168.55.20:8888/experiments'
```

### Get experiments data

```bash
scripts/get_experiments_data.sh 
```

## Data preprocessing
To calculate mean value of CPU, RAM and duration of all experiment iterations (per node) run `notebook/prepare_data.ipynb`.

This notebook creates individual plots for all iterations (per node) and plots for the mean values of all iterations.
Calculated mean RAM and CPU values, with a new column containing experiment mean duration time are stored in new file `experiment_mean_data.csv`
