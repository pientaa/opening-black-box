ARG SPARK_IMAGE=pienta/spark-submit:3.0.2

FROM ${SPARK_IMAGE} AS run

ENTRYPOINT cd /spark/bin && ./spark-submit --class BlackBox --master spark://spark-master:7077 --driver-memory 10g \
--deploy-mode cluster --conf spark.driver.extraJavaOptions='-Dconfig-path=/opt/spark-apps/dev/config.conf' /opt/spark-apps/black-box-assembly-1.0.jar postgres
