FROM flink:2.0-java21

COPY target/jdbc-flink-example-1.0-SNAPSHOT.jar /opt/flink/usrlib/jdbc-flink-example-1.0-SNAPSHOT.jar
COPY northwind/*    /opt/flink/csv-data/

RUN chown -R flink:flink /opt/flink