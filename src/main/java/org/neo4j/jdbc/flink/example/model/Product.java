package org.neo4j.jdbc.flink.example.model;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.jdbc.datasource.statements.SimpleJdbcQueryStatement;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.flink.util.jackson.JacksonMapperFactory;

@JsonPropertyOrder({
        "productId",
        "productName",
        "supplierId",
        "categoryId",
        "quantityPerUnit",
        "unitPrice",
        "unitsInStock",
        "unitsOnOrder",
        "reorderLevel",
        "discontinued"
})
public class Product {

    public int productId;
    public String productName;
    public int supplierId;
    public int categoryId;
    public String quantityPerUnit;
    public double unitPrice;
    public int unitsInStock;
    public int unitsOnOrder;
    public int reorderLevel;
    public String discontinued;

    public static CsvReaderFormat<Product> csvReaderFormat() {
        return CsvReaderFormat.forSchema(
                JacksonMapperFactory::createCsvMapper,
                (mapper) -> mapper.schemaFor(Product.class)
                        .withQuoteChar('\"')
                        .withHeader(),
                TypeInformation.of(Product.class));
    }

    public static SimpleJdbcQueryStatement<Product> queryStatement() {
        return new SimpleJdbcQueryStatement<>(
                "MERGE (n:Product{id: $1}) " +
                        "SET n += { " +
                        "   name: $2, " +
                        "   quantityPerUnit: $3, " +
                        "   unitPrice: $4, " +
                        "   unitsInStock: $5, " +
                        "   unitsOnOrder: $6, " +
                        "   reorderLevel: $7," +
                        "   discontinued: $8 " +
                        "}",
                (st, p) -> {
                    st.setInt(1, p.productId);
                    st.setString(2, p.productName);
                    st.setString(3, p.quantityPerUnit);
                    st.setDouble(4, p.unitPrice);
                    st.setInt(5, p.unitsInStock);
                    st.setInt(6, p.unitsOnOrder);
                    st.setInt(7, p.reorderLevel);
                    st.setString(8, p.discontinued);
                });
    }

}
