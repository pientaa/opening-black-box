import threading
import time
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

    def run(self):
        top_cmd = ['top','-b','-n 1', '-p', self.pids]
        tail_cmd = ['tail', '-n+8']
        # PID CPU RAM
        awk_cmd = ['awk', '{print $1 "\t" $9 "\t" $10}']
        
        while True:
            top_process = Popen(top_cmd, stdout=PIPE)
            tail_process = Popen(tail_cmd, stdin=top_process.stdout, stdout=PIPE)
            awk_process = Popen(awk_cmd, stdin=tail_process.stdout, stdout=PIPE)

            output = awk_process.communicate()[0]
            output = output.decode()

            if output:
                print(time.time())
                output = output.split("\n")
                print(output)

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
                return

        return
 



            # data_bytes = response_top.communicate()[0]
            # data = data_bytes.decode()
            # print('TH | Data from top: ', data)
