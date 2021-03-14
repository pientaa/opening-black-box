import threading
import time

class MonitorThread(threading.Thread):

    def __init__(self, *args, **kwargs):
        super(MonitorThread, self).__init__(*args, **kwargs)
        self._stop = threading.Event()

    def stop(self):
        print('Stopping thread!')
        self._stop.set()

    def stopped(self):
        return self._stop.isSet()

    def run(self):
        while True:
            if self.stopped():
                return
            print('Hello from new Thread ')
            time.sleep(1)