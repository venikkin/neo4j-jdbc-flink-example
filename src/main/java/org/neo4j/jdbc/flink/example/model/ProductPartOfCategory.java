package org.neo4j.jdbc.flink.example.model;

import org.apache.flink.connector.jdbc.datasource.statements.SimpleJdbcQueryStatement;

public class ProductPartOfCategory {

    public int productId;
    public int categoryId;

    public static ProductPartOfCategory fromProduct(Product product) {
        var productPartOfCategory = new ProductPartOfCategory();
        productPartOfCategory.productId = product.productId;
        productPartOfCategory.categoryId = product.categoryId;
        return productPartOfCategory;
    }

    public static SimpleJdbcQueryStatement<ProductPartOfCategory> queryStatement() {
        return new SimpleJdbcQueryStatement<>(
                "MATCH (p:Product {id: $1}) " +
                        "MATCH (c:Category{id: $2}) " +
                        "MERGE (p)-[:PART_OF]->(c)",
                (st, p) -> {
                    st.setInt(1, p.productId);
                    st.setInt(2, p.categoryId);
                });
    }

}
