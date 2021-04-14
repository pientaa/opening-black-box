FROM java:8-jdk-alpine

ENV DAEMON_RUN=true
ENV SPARK_VERSION=3.0.2
ENV HADOOP_VERSION=2.7
ENV SCALA_VERSION=2.12.13
ENV SCALA_HOME=/usr/share/scala

RUN apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    apk add --no-cache bash curl jq && \
    cd "/tmp" && \
    wget --no-verbose "https://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz" && \
    tar xzf "scala-${SCALA_VERSION}.tgz" && \
    mkdir "${SCALA_HOME}" && \
    rm "/tmp/scala-${SCALA_VERSION}/bin/"*.bat && \
    mv "/tmp/scala-${SCALA_VERSION}/bin" "/tmp/scala-${SCALA_VERSION}/lib" "${SCALA_HOME}" && \
    ln -s "${SCALA_HOME}/bin/"* "/usr/bin/" && \
    apk del .build-dependencies && \
    rm -rf "/tmp/"*

#Scala instalation
RUN export PATH="/usr/local/sbt/bin:$PATH" &&  apk update && apk add ca-certificates wget tar
#&& mkdir -p "/usr/local/sbt" && wget -qO - --no-check-certificate "https://github.com/sbt/sbt/releases/download/v0.13.18/sbt-0.13.18.tgz" | tar xz -C /usr/local/sbt --strip-components=1 && sbt sbtVersion
#RUN apk add --no-cache python3

RUN wget --no-verbose http://apache.mirror.iphh.net/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz && tar -xvzf spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
      && mv spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} spark \
      && rm spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz


ENV POSTGRES_URL https://repo1.maven.org/maven2/org/postgresql/postgresql/42.2.19/postgresql-42.2.19.jar
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
     && wget ${POSTGRES_URL} \
     && mv postgresql-42.2.19.jar /spark/jars \
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