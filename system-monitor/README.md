# System monitor

Python program monitoring docker container CPU and MEMORY usage.

## Run monitor

```bash
python system-monitor.py
```
## Load env

```bash
conda env create -f env.yml
conda activate env
```


## Start monitoring
Send ```POST``` request to endpoint ```http://localhost:8063/monitor```. Pass the container name to monitor and function name into request body:
```json
{
     "container_name": 'postgres',
     "monitor": true,
     "function_name": 'average'
}
```
## Stop monitoring
Send ```DELETE``` request to ```http://localhost:8063/monitor```. Request body:

```json
{
    "monitor": false
}
```

## Results
Monitoring result is saved to file ```average_timestamp.csv```.

| function_name   | timestamp                  | PID   | CPU  | RAM  |
| --------------- |:--------------------------:| -----:| ----:|-----:|
| average         | 2021-03-22 09:39:20.441156 | 3902  | 0.0  | 0.1  |