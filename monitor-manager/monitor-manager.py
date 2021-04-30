import json
import logging
import pandas as pd
import requests
import socket
import time
from csv import reader
from flask import Flask

app = Flask(__name__)


@app.route('/')
def health_check():
    return "Monitor Manager working"


@app.route('/experiments', methods=['post'])
def start_experiments():
    experiments_plan = pd.read_csv("opening-black-box/monitor-manager/experiments-plan.csv", delimiter=',')
    hosts_info = pd.read_csv("opening-black-box/monitor-manager/hosts-info.csv", delimiter=',')

    for index, row in experiments_plan.iterrows():
        for iteration in range(row["iterations"]):
            system_monitor(hosts_info, row['function_name'])

    return "Monitoring finished", 200


def system_monitor(hosts_info, function_name):
    logging.info("Task submitted")
    submit_response = requests.post("http://192.168.55.20:5000/submit", json={"function_name": function_name})
    submit_response = json.loads(submit_response.text)
    driver_id = submit_response["submissionId"]
    logging.info('Submit driver ID {} '.format(driver_id))

    for index, row in hosts_info.iterrows():
        url = "http://" + str(row['host_ip']) + ":8063/monitor"
        request_data = {
            "container_name": row['container_name'],
            "function_name": function_name
        }
        logging.info('Host {} : starting monitor'.format(row['host_ip']))
        response = requests.post(url, json=request_data)
        logging.info('Host {} : {} '.format(row['host_ip'], response.text))

    status = "RUNNING"
    while status != "FINISHED":
        status_response = requests.get("http://192.168.55.20:5000/status", json={"driver_id": driver_id})
        status_response = json.loads(status_response.text)
        status = status_response["driverState"]
        logging.info('Driver status {} '.format(status))
        time.sleep(1)

    logging.info("Experiment finished")
    time.sleep(3)
    for index, row in hosts_info.iterrows():
        url = "http://" + str(row['host_ip']) + ":8063/monitor"
        response = requests.delete(url)
        logging.info('Host {} : {} '.format(row['host_ip'], response.text))


if __name__ == "__main__":
    logging.basicConfig(filename='opening-black-box/monitor-manager/monitor-manager.log', level=logging.INFO,
                        format='%(asctime)s:%(message)s')
    app.run(debug=True, host='0.0.0.0', port=8888)
