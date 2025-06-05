package org.neo4j.jdbc.flink.example.model;

import org.apache.flink.connector.jdbc.datasource.statements.SimpleJdbcQueryStatement;

public class SupplierSuppliesProduct {

    public int supplierId;
    public int productId;

    public static SupplierSuppliesProduct fromProduct(Product product) {
        var supplierSuppliesProduct = new SupplierSuppliesProduct();
        supplierSuppliesProduct.productId = product.productId;
        supplierSuppliesProduct.supplierId = product.supplierId;
        return supplierSuppliesProduct;
    }

    public static SimpleJdbcQueryStatement<SupplierSuppliesProduct> queryStatement() {
        return new SimpleJdbcQueryStatement<>(
                "MATCH (s:Supplier {id: $1}) " +
                        "MATCH (p:Product{id: $2}) " +
                        "MERGE (s)-[:SUPPLIES]->(p)",
                (st, p) -> {
                    st.setInt(1, p.supplierId);
                    st.setInt(2, p.productId);
                });
    }

}
