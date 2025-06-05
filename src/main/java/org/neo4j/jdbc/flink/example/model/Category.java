package org.neo4j.jdbc.flink.example.model;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.jdbc.datasource.statements.SimpleJdbcQueryStatement;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.flink.util.jackson.JacksonMapperFactory;

@JsonPropertyOrder({
        "categoryId",
        "categoryName",
        "description",
        "picture"
})
public class Category {

    public int categoryId;
    public String categoryName;
    public String description;
    public String picture;

    public static CsvReaderFormat<Category> csvReaderFormat() {
        return CsvReaderFormat.forSchema(
                JacksonMapperFactory::createCsvMapper,
                (mapper) -> mapper.schemaFor(Category.class)
                        .withQuoteChar('\"')
                        .withHeader(),
                TypeInformation.of(Category.class));
    }

    public static SimpleJdbcQueryStatement<Category> queryStatement() {
        return new SimpleJdbcQueryStatement<>(
                "MERGE (n:Category {id: $1}) " +
                        "SET n += {name: $2, description: $3, picture: $4}",
                (st, c) -> {
                    st.setInt(1, c.categoryId);
                    st.setString(2, c.categoryName);
                    st.setString(3, c.description);
                    st.setString(4, c.picture);
                });
    }
}