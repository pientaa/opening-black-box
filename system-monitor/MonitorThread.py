import threading
import time
import os
from datetime import datetime
from subprocess import Popen, PIPE
import logging


class MonitorThread(threading.Thread):

    def __init__(self, *args, **kwargs):
        super(MonitorThread, self).__init__(*args, **kwargs)
        self._stop = threading.Event()

    def stop(self):
        print('TH | Stopping thread!')
        self._stop.set()

    def is_stopped(self):
        return self._stop.isSet()

    def set_pids(self, pids):
        self.pids = pids

    def set_function_name(self, name):
        self.function_name = name

    def run(self):
        top_cmd = ['top', '-b', '-n 1', '-p', self.pids]
        tail_cmd = ['tail', '-n+8']
        awk_cmd = ['awk', '{print $1 "\t" $9 "\t" $10}']

        date_time = datetime.now()
        experiment_datetime = date_time.strftime("%d_%m_%Y_%H_%M_%S")
        path = "./opening-black-box/experiments/" + self.function_name
        try:
            os.mkdir("./opening-black-box/experiments")
            os.mkdir(path)
        except:
            logging.warning("Directory already exists")

        file = open(path + "/" + experiment_datetime + ".csv", "a")
        file.write("timestamp,PID,CPU,RAM\n")
        while True:
            top_process = Popen(top_cmd, stdout=PIPE)
            tail_process = Popen(tail_cmd, stdin=top_process.stdout, stdout=PIPE)
            awk_process = Popen(awk_cmd, stdin=tail_process.stdout, stdout=PIPE)

            output = awk_process.communicate()[0]
            output = output.decode()

            if output:
                output = [i.replace(",", ".").replace("\t", ",") for i in output.split("\n")]
                output = output[:-1]
                timestamp = datetime.now()
                for x in output:
                    row = str(timestamp) + "," + x + "\n"
                    file.write(row)

            top_process.stdout.close()
            top_process.kill()

            tail_process.stdout.close()
            tail_process.kill()

            awk_process.stdout.close()
            awk_process.kill()

            if self.is_stopped():
                logging.info("Thread stoppping")
                file.close()
                return

        return
