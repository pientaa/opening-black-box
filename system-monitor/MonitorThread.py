import threading
import time
from datetime import datetime
from subprocess import Popen, PIPE

class MonitorThread(threading.Thread):

    def __init__(self, *args, **kwargs):
        super(MonitorThread, self).__init__(*args, **kwargs)
        self._stop = threading.Event()

    def stop(self):
        print('TH | Stopping thread!')
        self._stop.set()

    def stopped(self):
        return self._stop.isSet()

    def set_pids(self, pids):
        self.pids = pids
    
    def set_function_name(self, name):
        self.function_name = name

    def run(self):
        top_cmd = ['top','-b','-n 1', '-p', self.pids]
        tail_cmd = ['tail', '-n+8']
        # PID CPU RAM
        awk_cmd = ['awk', '{print $1 "\t" $9 "\t" $10}']
        
        # function_name, timestamp, PID, CPU, RAM
        # ['3662\t0,0\t0,1', '3735\t0,0\t0,0', '3736\t0,0\t0,0', '3737\t0,0\t0,0', '3738\t0,0\t0,0', '3739\t0,0\t0,0', '']
        experiment_datetime = datetime.now()
        experiment_datetime = experiment_datetime.strftime("%d_%m_%Y_%H_%M_%S")
        file = open(self.function_name + "_" + experiment_datetime + ".csv", "a")
        file.write("function_name,timestamp,PID,CPU,RAM\n")
        while True:
            top_process = Popen(top_cmd, stdout=PIPE)
            tail_process = Popen(tail_cmd, stdin=top_process.stdout, stdout=PIPE)
            awk_process = Popen(awk_cmd, stdin=tail_process.stdout, stdout=PIPE)

            output = awk_process.communicate()[0]
            output = output.decode()


            if output:
                output = [i.replace(",",".").replace("\t",",") for i in output.split("\n")]
                output  = output[:-1]
                timestamp = datetime.now()
                for x in output:
                    row = self.function_name + "," + str(timestamp) + "," + x + "\n"
                    file.write(row)

            top_process.stdout.close()
            top_process.kill()
            top_process.wait()

            tail_process.stdout.close()
            tail_process.kill()
            tail_process.wait()

            awk_process.stdout.close()
            awk_process.kill()
            awk_process.wait()

            if self.stopped():
                print("TH | Thread stopping!")
                file.close()
                return

        return