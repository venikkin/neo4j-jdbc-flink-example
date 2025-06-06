# Example of Neo4j JDBC driver usage with Apache Flink

## Usage 
1. Build
```
mvn clean install
```
2. Deploy
```
docker compose up --build -d
```
3. Run the job
```
docker exec neo4j-jdbc-flink-example-jobmanager-1 bash -c "flink run /opt/flink/usrlib/jdbc-flink-example-1.0-SNAPSHOT.jar"
```
4. Clean up
```
docker compose down -v
```