import pandas as pd
import sys
import csv
import datetime
from datetime import datetime, date
from datetime import timedelta
import calendar
import pandas as pd
import matplotlib.pyplot as plt
from pandas.plotting import table 

def resources_statistics(function, table):
    print(function)
    print(table)

    cpu = []
    mem = []
    cpu_tmp = []
    mem_tmp = []
    cpu_mean = []
    mem_mean = []
    cpu_std = []
    mem_std = []
    i = 0
    t = []

# MEAN CALCULATIONS
    for j in range(1, 31):
        i = 0
        cpu_tmp = []
        mem_tmp = []
        with open('./' + function + '/' + table + '/metrics_' + str(j) + '.csv','r') as csvfile:
            plots = csv.reader(csvfile, delimiter='\t')

            for row in plots:
                if(j == 1):
                    # FIRST ROW IS A HEADER
                    if(i != 0):
                        cpu_mean.append(float(row[1].replace(',','.'))/12)
                        mem_mean.append(float(row[2].replace(',','.'))*(32.0/10.0))
                        if(i == 1):
                            begin_epoch=int(row[0])
                        t.append((int(row[0]) - begin_epoch)/1000.0)
                    # t.append(datetime.fromtimestamp(int(row[0])//1000).replace(microsecond=int(row[0])%1000*1000))
                else:
                    if(i != 0):
                        if(i == 1):
                            begin_epoch=int(row[0])
                        if(len(cpu_mean) >= i):
                            cpu_new = float("{:.2f}".format((cpu_mean[i-1] * (j - 1) + (float(row[1].replace(',','.')))/12) / (j))) 
                            mem_new = float("{:.2f}".format((mem_mean[i-1] * (j - 1) + (float(row[2].replace(',','.')))*(32.0/10.0)) / (j)))
                            cpu_mean[i-1] = cpu_new
                            mem_mean[i-1] = mem_new                        
                        else:
                            cpu_mean.append(float(row[1].replace(',','.'))/12)
                            mem_mean.append(float(row[2].replace(',','.'))*(32.0/10.0))
                            t.append((int(row[0]) - begin_epoch)/1000.0)
                    #   t.append(pd.to_datetime(int(row[0]), unit='ms').to_pydatetime())
                            # t.append(datetime.fromtimestamp(int(row[0])//1000.0).replace(microsecond=int(row[0])%1000*1000))
                # if(i != 0):
                #     cpu_tmp.append(float(row[1].replace(',','.'))/12)
                #     mem_tmp.append(float(row[2].replace(',','.'))*(32.0/10.0)) 
                i += 1
                # if(i>max_length):
                #     max_length=i
                #     t_temp = []
                #     for row in plots:
                #         t_temp.append(datetime.fromtimestamp(int(row[0])//1000).replace(microsecond=int(row[0])%1000*1000))
                #     t=t_temp
        # cpu.append(cpu_tmp)
        # mem.append(mem_tmp)  

# STANDARD DEVIATION CALCULATIONS
    # for i in range(0, len(cpu_mean)):
    #     cpu_std_temp = 0
    #     mem_std_temp = 0
    #     for j in range(0 , len(cpu)):
    #         cpu_array = cpu[j]
    #         mem_array = mem[j]

    #         if(len(cpu_array)<=i):
    #             # in order to see better statistics
    #             cpu_array.append(cpu_mean[i])
    #         if(len(mem_array)<=i):
    #             mem_array.append(mem_mean[i])    

    #         cpu_std_temp += (cpu_mean[i] - cpu_array[i])**2
    #         mem_std_temp += (mem_mean[i] - mem_array[i])**2
    #     cpu_std_temp = (cpu_std_temp / len(cpu_array))**(0.5)
    #     mem_std_temp = (mem_std_temp / len(mem_array))**(0.5)
    #     cpu_std.append(cpu_std_temp)
    #     mem_std.append(mem_std_temp)
    
    print(cpu_mean)
    print(mem_mean)
    # print(cpu_std)
    # print(mem_std)

    # t = []
    # for i in range(0, len(cpu_mean)):
    #     t.append(i/10)

    plt.plot(t,cpu_mean,label="Mean value")
    # plt.plot(t,cpu_std,label="Standard deviation")
    plt.xlabel('Time [s]')
    plt.ylabel('CPU usage [%]')
    # plt.title(function + "\n" + table)
    plt.legend()
    plt.savefig('./' + function + '/' + table + '/' + "cpu.png")

    plt.clf()
    plt.plot(t,mem_mean,label="Mean value")
    # plt.plot(t,mem_std,label="Standard deviation")
    plt.xlabel('Time [s]')
    plt.ylabel('Memory usage [%]')
    # plt.title(function + "\n" + table)
    plt.legend()
    plt.savefig('./' + function + '/' + table + '/' + "mem.png")

if __name__== "__main__":
    resources_statistics(sys.argv[1], sys.argv[2])