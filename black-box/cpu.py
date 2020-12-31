import matplotlib.pyplot as plt
import csv
import sys


def cpu(function, table, path):
    x = []
    y = []
    i = 0

    with open('metrics.csv','r') as csvfile:
        plots = csv.reader(csvfile, delimiter='\t')
        for row in plots:
            if(i != 0):
                x.append(i/10)
                y.append(float(row[1].replace(',','.'))/12.0)
            i += 1 


    plt.plot(x,y,label="CPU") #, label='Loaded from file!'
    plt.xlabel('Time [s]')
    plt.ylabel('CPU usage [%]')
    plt.title(function + "\n" + table)
    plt.legend()
    # plt.show()
    file_name = function + "_" + table + "_cpu.png"
    print(file_name)
    plt.savefig(path + file_name)

if __name__== "__main__":
    cpu(sys.argv[1], sys.argv[2], sys.argv[3])