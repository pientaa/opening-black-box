import matplotlib.pyplot as plt
import csv
import sys
# Take argument to save img as cpu_test_input_100_filterFromMondayToThursday.png

# Save png to another folder like /experiments

def memory(function, table, path):

    print(function)
    print(table)

    x = []
    y = []
    i = 0

    with open('metrics.csv','r') as csvfile:
        plots = csv.reader(csvfile, delimiter='\t')
        for row in plots:
            if(i != 0):
                x.append(i/10)
                y.append(float(row[2].replace(',','.')))
            i += 1 


    plt.plot(x,y, label="MEMORY") #, label='Loaded from file!'
    plt.xlabel('Time [s]')
    plt.ylabel('MEM usage [%]')
    plt.title(function + " " + table)
    plt.legend()
    # plt.show()
    file_name = function + "_" + table + "_mem.png"
    plt.savefig(path + file_name)


if __name__== "__main__":
    memory(sys.argv[1], sys.argv[2], sys.argv[3])