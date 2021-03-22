import subprocess
import sys 
import time
import threading
from flask import Flask, request
from MonitorThread import MonitorThread

app = Flask(__name__)

monitor_thread = None

# {
#     "monitor": false
# }

@app.route('/monitor', methods=['delete'])
def stop_monitor():
    try:
        try:
            data = request.get_json()
            if not data['monitor']:
                global monitor_thread
                monitor_thread.stop()
                
        except:
            raise ValueError

    except ValueError:
        return 'No request body / No active monitoring', 400
    
    return 'Monitoring stopped', 200
        
# {
#     "container_name": 'name',
#     "monitor": false
#     "function_name": 'name'
# }

@app.route('/monitor', methods=['post'])
def system_monitor():
    try:
        try:
            data = request.get_json()
        except:
            raise ValueError

        if data is None:
            raise ValueError

    except ValueError:
        return 'Request data is not in json or is null', 400

    global monitor
    container_name = data["container_name"]
    monitor = data["monitor"]
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

    print("M | pids ", pids)

    global monitor_thread
    monitor_thread = MonitorThread()
    monitor_thread.set_pids(pids)
    monitor_thread.set_function_name(function_name)
    monitor_thread.start()


    print('M | Terminating subprocesses!')
    response_docker_top.stdout.close()
    response_docker_top.kill()
    response_docker_top.wait()

    response_awk.stdout.close()
    response_awk.kill()
    response_awk.wait()

    pids_col.stdout.close() 
    pids_col.kill()
    pids_col.wait()

    
    return 'Monitoring started', 202

    # top "${pids[@]/#/-p }" -b -n 1 | tail -n+8
# run(host='localhost', port=8063, debug=True, reloader=True)
app.run(port=8063)

