#Environment variables used by the spark workers
#Do not touch this unless you modify the compose master
SPARK_MASTER=spark://spark-master:7077
#Allocation Parameters
SPARK_WORKER_CORES=2
SPARK_WORKER_MEMORY=6G
SPARK_DRIVER_MEMORY=6G
SPARK_EXECUTOR_MEMORY=6G