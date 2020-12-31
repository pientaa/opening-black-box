import pandas as pd
import sys
import matplotlib.pyplot as plt
from pandas.plotting import table

def time_statistics(function, table):

    df = pd.read_csv(table + "_" + function + ".csv", delimiter=',', names=['id', 'time'], index_col=False).drop(['id'], axis=1)
    df.describe().to_csv("stats_" + table + "_" + function + ".csv")


if __name__== "__main__":
    time_statistics(sys.argv[1], sys.argv[2])