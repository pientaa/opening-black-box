# System monitor

Python program monitoring docker container CPU and MEMORY usage.

## Load env

```bash
conda env create -f system-monitor-env.yml
conda activate system-monitor
```

## Run monitor

```bash
python system-monitor.py
```

## Start monitoring
Send ```POST``` request to endpoint ```http://localhost:8063/monitor```. Pass the container name to monitor and function name into request body:
```json
{
     "container_name": "postgres",
     "function_name": "average"
}
```

## Stop monitoring
Send ```DELETE``` request to ```http://localhost:8063/monitor```. 


## Results
Monitoring result is saved to file ```average_timestamp.csv```.

| function_name   | timestamp                  | PID   | CPU  | RAM  |
| --------------- |:--------------------------:| -----:| ----:|-----:|
| average         | 2021-03-22 09:39:20.441156 | 3902  | 0.0  | 0.1  |