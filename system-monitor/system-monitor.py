import subprocess
from bottle import run, post, get, response, request

# {
#     "container_name": 'name',
#     "monitor": false
# }

@post('/monitor')
def system_monitor():

    try:
        try:
            data = request.json();
        except:
            raise ValueError

        if data is None:
            raise ValueError

    except ValueError:
        response.status = 400
        return

    container_name = data["container_name"]
    monitor = data["monitor"]

    first_cmd = ['docker', 'top', container_name]
    second_cmd = ['awk', '{print $2}']
    third_cmd = ['tail', '-n+2']

    p1 = subprocess.Popen(first_cmd, stdout=subprocess.PIPE)
    p2 = subprocess.Popen(second_cmd, stdin=p1.stdout, stdout=subprocess.PIPE)
    p3 = subprocess.Popen(third_cmd, stdin=p2.stdout, stdout=subprocess.PIPE)

    pids, stderr = p3.communicate().decode()

    # top "${pids[@]/#/-p }" -b -n 1 | tail -n+8

run(host='localhost', port=8063, debug=True, reloader=True)