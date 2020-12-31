ARG SPARK_IMAGE=gcr.io/spark-operator/spark:v2.4.5

FROM ${SPARK_IMAGE} AS run

COPY ./target/scala-2.11/ /usr/src/app

ENV POST_URL https://jdbc.postgresql.org/download/postgresql-42.2.12.jar
ENV SPARKJAVA_URL https://repo1.maven.org/maven2/com/sparkjava/spark-core/1.0/spark-core-1.0.jar
ENV SCALA_J https://repo1.maven.org/maven2/org/scalaj/scalaj-http_2.11/2.4.2/scalaj-http_2.11-2.4.2.jar
ENV JETTY_SERVER_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-server/9.0.2.v20130417/jetty-server-9.0.2.v20130417.jar
ENV JETTY_UTIL_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-util/9.0.2.v20130417/jetty-util-9.0.2.v20130417.jar
ENV JETTY_IO_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-io/9.0.2.v20130417/jetty-io-9.0.2.v20130417.jar
ENV JETTY_HTTP_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-http/9.0.2.v20130417/jetty-http-9.0.2.v20130417.jar
ENV JETTY_SECURITY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-security/9.0.2.v20130417/jetty-security-9.0.2.v20130417.jar
ENV JETTY_SERVLET_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-servlet/9.0.2.v20130417/jetty-servlet-9.0.2.v20130417.jar
ENV JETTY_WEBAPP_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-webapp/9.0.2.v20130417/jetty-webapp-9.0.2.v20130417.jar
ENV JETTY_XML_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-xml/9.0.2.v20130417/jetty-xml-9.0.2.v20130417.jar


RUN apt-get update \
     && apt-get install -y wget \
     && wget ${POST_URL} \
     && mv postgresql-42.2.12.jar /opt/spark/jars \
     && wget ${SPARKJAVA_URL} \
     && mv spark-core-1.0.jar /opt/spark/jars \
     && wget ${JETTY_SERVER_URL} \
     && mv jetty-server-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_UTIL_URL} \
     && mv jetty-util-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_IO_URL} \
     && mv jetty-io-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_HTTP_URL} \
     && mv jetty-http-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_SECURITY_URL} \
     && mv jetty-security-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_SERVLET_URL} \
     && mv jetty-servlet-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_WEBAPP_URL} \
     && mv jetty-webapp-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${JETTY_XML_URL} \
     && mv jetty-xml-9.0.2.v20130417.jar /opt/spark/jars \
     && wget ${SCALA_J} \
     && mv scalaj-http_2.11-2.4.2.jar /opt/spark/jars

ENTRYPOINT cd $SPARK_HOME/bin/ && ./spark-submit --class ExperimentExecutor --master local[2] --driver-memory 10g \
/usr/src/app/udf-detector-assembly-1.0.jar postgres black-box
