import subprocess
import sys 
import time
import threading
from bottle import run, post, delete, get, response, request
from MonitorThread import MonitorThread

monitor_thread = None

# {
#     "monitor": false
# }

@delete('/monitor')
def stop_monitor():
    data = request.json
    if not data['monitor']:
        global monitor_thread
        monitor_thread.stop()


# {
#     "container_name": 'name',
#     "monitor": false
# }

@post('/monitor')
def system_monitor():
    print("Here")
    try:
        try:
            data = request.json
        except:
            raise ValueError

        if data is None:
            raise ValueError

    except ValueError:
        response.status = 400
        return 'Request data is not in json or is null'

    global monitor
    container_name = data["container_name"]
    monitor = data["monitor"]

    first_cmd = ['docker', 'top', container_name]
    second_cmd = ['awk', '{print $2}']
    third_cmd = ['tail', '-n+2']

    response_docker_top = subprocess.Popen(first_cmd, stdout=subprocess.PIPE)
    response_awk = subprocess.Popen(second_cmd, stdin=response_docker_top.stdout, stdout=subprocess.PIPE)
    pids_col = subprocess.Popen(third_cmd, stdin=response_awk.stdout, stdout=subprocess.PIPE)

    pids_bytes, pids_err = pids_col.communicate()
    pids = pids_bytes.decode()

    pref_str = "-p "
    suf_str = " "

    pids = pids.split('\n')
    pids = [pref_str + sub + suf_str for sub in pids]
    del pids[-1]
    pids = ''.join(pids)

    global monitor_thread
    monitor_thread = MonitorThread()
    monitor_thread.start()


    print('Terminating subprocesses!')
    response_docker_top.stdout.close()
    response_docker_top.kill()
    response_docker_top.wait()

    response_awk.stdout.close()
    response_awk.kill()
    response_awk.wait()

    pids_col.stdout.close() 
    pids_col.kill()
    pids_col.wait()

    response.status = 202
    return 'Monitoring started'

    # top "${pids[@]/#/-p }" -b -n 1 | tail -n+8
run(host='localhost', port=8063, debug=True, reloader=True)

