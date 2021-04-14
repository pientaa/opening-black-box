from csv import reader
from flask import Flask
import socket
import json
import requests
import time

app = Flask(__name__)


@app.route('/', methods=['get'])
def health_check():
    return "Monitor Manager working"


@app.route('/experiments', methods=['post'])
def start_experiments():
    experiments_plan = read_csv("experiments-plan.csv")
    hosts_info = read_csv("hosts-info.csv")
    is_spark_node, host_ip = check_if_spark_node(hosts_info)

    for function_name, table in experiments_plan:
        # if is_spark_node:
        print("Task submitting")
        submit_response = requests.post("http://192.168.55.20:5000/submit", json={"function_name": function_name})
        submit_response = json.loads(submit_response.text)
        driver_id = submit_response["submissionId"]
        print("Submit driver ID: ", driver_id)

        print("Start monitoring")
        system_monitor(host_ip, hosts_info, function_name, True)
        #
        status = "RUNNING"
        while status == "RUNNING":
            status_response = requests.get("http://192.168.55.20:5000/status", json={"driver_id": driver_id})
            status_response = json.loads(status_response.text)
            status = status_response["driverState"]
            time.sleep(1)

        system_monitor(host_ip, hosts_info, function_name, False)

    return "Monitoring finished", 200


def system_monitor(host_ip, hosts_info, function_name, start):
    for ip, container_name in hosts_info:
        url = "http://" + str(ip) + ":8063/monitor"
        request_data = {
            "container_name": container_name,
            "function_name": function_name
        }
        if start:
            response = requests.post(url, json=request_data)
            print(response.text)
        else:
            response = requests.delete(url)
        print("IP: ", ip, " ", response.text)


def read_csv(filename):
    with open(filename, 'r') as file:
        file_reader = reader(file, delimiter=',')
        header = next(file_reader)
        return list(file_reader)


def check_if_spark_node(hosts_info):
    hostname = socket.gethostname()
    ip_address = socket.gethostbyname(hostname)
    for host_data in hosts_info:
        if ip_address == host_data[0]:
            return True, ip_address
    return False, ip_address


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8888)
