import threading
import time
import subprocess

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
        top_cmd = ['top','-b', '-p', self.pids]
        tail_cmd = ['tail', '-n+8']
        top_process = subprocess.Popen(top_cmd, stdout=subprocess.PIPE)
        while True:
            if self.stopped():
                print("T | Stopped!")
                top_process.stdout.close()
                top_process.kill()
                top_process.wait()
                return
            
            print("TH | Here!")
            output = top_process.stdout.readlines()

            if output == '' and top_process.poll() is not None:
                print("TH |  Killing top!")
                break
            
            print("TH | Here 2 !")
            if output:
                print("TH | Here 3 !")
                print(output.strip())           



            # data_bytes = response_top.communicate()[0]
            # data = data_bytes.decode()
            # print('TH | Data from top: ', data)
