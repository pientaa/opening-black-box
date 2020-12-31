#!/bin/bash
SPARK_APPLICATION_JAR_LOCATION="/spark/jars/black-box-assembly.jar"
SPARK_APPLICATION_MAIN_CLASS="BlackBox"
SPARK_SUBMIT_ARGS="--conf spark.executor.extraJavaOptions='-Dconfig-path=/opt/spark-apps/dev/config.conf'"

docker run --network sparkconfig_spark-network -p 4567:4567 -v /mnt/spark-apps:/opt/spark-apps --env SPARK_APPLICATION_JAR_LOCATION=$SPARK_APPLICATION_JAR_LOCATION --env SPARK_APPLICATION_MAIN_CLASS=$SPARK_APPLICATION_MAIN_CLASS spark-submit:2.4.7