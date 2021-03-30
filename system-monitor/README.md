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
Monitoring result is saved to file ```timestamp.csv```.
Where: 
* timestamp - monitoring start time,
* PID - process ID,
* CPU - the percentage of CPU that is being used by the process. By default 'top' displays this as a percentage of a single CPU. On multi-core systems, percentages are greater than 100%,
* RAM - process currently resident share of available physical memory.

| timestamp                  | PID   | CPU  | RAM  |
|:--------------------------:| -----:| ----:|-----:|
| 2021-03-22 09:39:20.441156 | 3902  | 0.0  | 0.1  |