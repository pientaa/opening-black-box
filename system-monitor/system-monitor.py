import subprocess
import logging
from flask import Flask, request
from waitress import serve

from MonitorThread import MonitorThread

app = Flask(__name__)

monitor_thread = None


@app.route('/monitor', methods=['delete'])
def stop_monitor():
    global monitor_thread
    monitor_thread.stop()

    return 'monitoring stopped', 200


@app.route('/monitor', methods=['post'])
def start_monitor():
    try:
        try:
            data = request.get_json()
        except:
            raise ValueError

        if data is None:
            raise ValueError

    except ValueError:
        return 'Request data is not in json or is null', 400

    container_name = data["container_name"]
    function_name = data["function_name"]

    first_cmd = ['docker', 'top', container_name]
    second_cmd = ['awk', '{print $2}']
    third_cmd = ['tail', '-n+2']

    response_docker_top = subprocess.Popen(first_cmd, stdout=subprocess.PIPE)
    response_awk = subprocess.Popen(second_cmd, stdin=response_docker_top.stdout, stdout=subprocess.PIPE)
    pids_col = subprocess.Popen(third_cmd, stdin=response_awk.stdout, stdout=subprocess.PIPE)

    pids_bytes, pids_err = pids_col.communicate()
    pids = pids_bytes.decode()

    pids = pids.split('\n')
    pids = ','.join(pids)
    pids = pids[:-1]

    global monitor_thread
    monitor_thread = MonitorThread()
    monitor_thread.set_pids(pids)
    monitor_thread.set_function_name(function_name)
    monitor_thread.start()

    logging.info('Terminating subprocesses')
    response_docker_top.stdout.close()
    response_docker_top.kill()

    response_awk.stdout.close()
    response_awk.kill()

    pids_col.stdout.close()
    pids_col.kill()

    return 'Monitoring started', 202


if __name__ == "__main__":
    logging.basicConfig(filename='./opening-black-box/system-monitor.log', level=logging.DEBUG, format='%(asctime)s:%(message)s')
    serve(app, host="0.0.0.0", port=8063)
