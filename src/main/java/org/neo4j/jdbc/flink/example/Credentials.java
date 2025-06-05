package org.neo4j.jdbc.flink.example;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;

public class Credentials {

    public final static JdbcConnectionOptions sinkJdbcOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
            .withDriverName("org.neo4j.jdbc.Neo4jDriver")
                .withUrl("jdbc:neo4j://neo4j:7687")
                .withPassword("password123")
                .withUsername("neo4j")
                .build();

}
