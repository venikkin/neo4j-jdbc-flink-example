package org.neo4j.jdbc.flink.example;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.core.datastream.sink.JdbcSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.neo4j.jdbc.flink.example.model.*;

import java.io.File;

public class Northwind {

    public static void main(String[] args) throws Exception {
        importNodes();

        importPartOfRelationship();
        importSuppliesRelationship();
    }

    private static void importNodes() throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        var productSource = env.fromSource(
                FileSource.forRecordStreamFormat(Product.csvReaderFormat(),
                                Path.fromLocalFile(new File("/opt/flink/csv-data/products.csv")))
                        .build(),
                WatermarkStrategy.noWatermarks(),
                "products");

        productSource.sinkTo(
                JdbcSink.<Product>builder()
                        .withExecutionOptions(JdbcExecutionOptions.builder()
                                .withBatchSize(500)
                                .build())
                        .withQueryStatement(Product.queryStatement())
                        .buildAtLeastOnce(Credentials.sinkJdbcOptions)
        );

        var categoriesSource = env.fromSource(
                FileSource.forRecordStreamFormat(Category.csvReaderFormat(),
                                Path.fromLocalFile(new File("/opt/flink/csv-data/categories.csv")))
                        .build(),
                WatermarkStrategy.noWatermarks(),
                "categories");

        categoriesSource.sinkTo(
                JdbcSink.<Category>builder()
                        .withExecutionOptions(JdbcExecutionOptions.builder()
                                .withBatchSize(500)
                                .build())
                        .withQueryStatement(Category.queryStatement())
                        .buildAtLeastOnce(Credentials.sinkJdbcOptions)
        );

        var supplierSource = env.fromSource(
                FileSource.forRecordStreamFormat(Supplier.csvReaderFormat(),
                                Path.fromLocalFile(new File("/opt/flink/csv-data/suppliers.csv")))
                        .build(),
                WatermarkStrategy.noWatermarks(),
                "suppliers");

        supplierSource.sinkTo(
                JdbcSink.<Supplier>builder()
                        .withExecutionOptions(JdbcExecutionOptions.builder()
                                .withBatchSize(500)
                                .build())
                        .withQueryStatement(Supplier.queryStatement())
                        .buildAtLeastOnce(Credentials.sinkJdbcOptions)
        );

        env.execute("Northwind import - Nodes");
    }

    private static void importPartOfRelationship() throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        var productPartOfCategorySource = env.fromSource(
                FileSource.forRecordStreamFormat(Product.csvReaderFormat(),
                                Path.fromLocalFile(new File("/opt/flink/csv-data/products.csv")))
                        .build(),
                WatermarkStrategy.noWatermarks(),
                "products")
                .map(ProductPartOfCategory::fromProduct);

        productPartOfCategorySource.sinkTo(
                JdbcSink.<ProductPartOfCategory>builder()
                        .withExecutionOptions(JdbcExecutionOptions.builder()
                                .withBatchSize(500)
                                .build())
                        .withQueryStatement(ProductPartOfCategory.queryStatement())
                        .buildAtLeastOnce(Credentials.sinkJdbcOptions)
        );

        env.execute("Northwind import - PART_OF relationship");
    }

    private static void importSuppliesRelationship() throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        var supplierSuppliesProductSource = env.fromSource(
                        FileSource.forRecordStreamFormat(Product.csvReaderFormat(),
                                        Path.fromLocalFile(new File("/opt/flink/csv-data/products.csv")))
                                .build(),
                        WatermarkStrategy.noWatermarks(),
                        "products")
                .map(SupplierSuppliesProduct::fromProduct);

        supplierSuppliesProductSource.sinkTo(
                JdbcSink.<SupplierSuppliesProduct>builder()
                        .withExecutionOptions(JdbcExecutionOptions.builder()
                                .withBatchSize(500)
                                .build())
                        .withQueryStatement(SupplierSuppliesProduct.queryStatement())
                        .buildAtLeastOnce(Credentials.sinkJdbcOptions)
        );

        env.execute("Northwind import - SUPPLIES relationship");
    }


}
