FROM pienta/spark-base:2.4.7

COPY ./start-worker.sh /

ENV SPARK_WORKER_WEBUI_PORT 8081
ENV SPARK_WORKER_LOG /spark/logs
ENV SPARK_MASTER "spark://spark-master:7077"

ENV POST_URL https://jdbc.postgresql.org/download/postgresql-42.2.12.jar
ENV SPARKJAVA_URL https://repo1.maven.org/maven2/com/sparkjava/spark-core/1.0/spark-core-1.0.jar
ENV JETTY_SERVER_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-server/9.0.2.v20130417/jetty-server-9.0.2.v20130417.jar
ENV JETTY_UTIL_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-util/9.0.2.v20130417/jetty-util-9.0.2.v20130417.jar
ENV JETTY_IO_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-io/9.0.2.v20130417/jetty-io-9.0.2.v20130417.jar
ENV JETTY_HTTP_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-http/9.0.2.v20130417/jetty-http-9.0.2.v20130417.jar
ENV JETTY_SECURITY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-security/9.0.2.v20130417/jetty-security-9.0.2.v20130417.jar
ENV JETTY_SERVLET_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-servlet/9.0.2.v20130417/jetty-servlet-9.0.2.v20130417.jar
ENV JETTY_WEBAPP_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-webapp/9.0.2.v20130417/jetty-webapp-9.0.2.v20130417.jar
ENV JETTY_XML_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-xml/9.0.2.v20130417/jetty-xml-9.0.2.v20130417.jar
ENV SPARK_MEASURE_URL https://repo1.maven.org/maven2/ch/cern/sparkmeasure/spark-measure_2.11/0.17/spark-measure_2.11-0.17.jar

RUN apk update \
     && wget ${POST_URL} \
     && mv postgresql-42.2.12.jar /spark/jars \
     && wget ${SPARKJAVA_URL} \
     && mv spark-core-1.0.jar /spark/jars \
     && wget ${JETTY_SERVER_URL} \
     && mv jetty-server-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_UTIL_URL} \
     && mv jetty-util-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_IO_URL} \
     && mv jetty-io-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_HTTP_URL} \
     && mv jetty-http-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_SECURITY_URL} \
     && mv jetty-security-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_SERVLET_URL} \
     && mv jetty-servlet-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_WEBAPP_URL} \
     && mv jetty-webapp-9.0.2.v20130417.jar /spark/jars \
     && wget ${JETTY_XML_URL} \
     && mv jetty-xml-9.0.2.v20130417.jar /spark/jars \
     && wget ${SPARK_MEASURE_URL} \
     && mv spark-measure_2.11-0.17.jar /spark/jars

EXPOSE 8081

CMD ["/bin/bash", "/start-worker.sh"]