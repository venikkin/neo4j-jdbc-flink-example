package org.neo4j.jdbc.flink.example.model;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.jdbc.datasource.statements.SimpleJdbcQueryStatement;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.flink.util.jackson.JacksonMapperFactory;

@JsonPropertyOrder({
        "supplierId",
        "companyName",
        "contactName",
        "contactTitle",
        "address",
        "city",
        "region",
        "postalCode",
        "country",
        "phone",
        "fax",
        "homePage",
})
public class Supplier {

    public int supplierId;
    public String companyName;
    public String contactName;
    public String contactTitle;
    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String phone ;
    public String fax;
    public String homePage;

    public static CsvReaderFormat<Supplier> csvReaderFormat() {
        return CsvReaderFormat.forSchema(
                JacksonMapperFactory::createCsvMapper,
                (mapper) -> mapper.schemaFor(Supplier.class)
                        .withQuoteChar('\"')
                        .withHeader(),
                TypeInformation.of(Supplier.class));
    }

    public static SimpleJdbcQueryStatement<Supplier> queryStatement() {
        return new SimpleJdbcQueryStatement<>(
                "MERGE (n:Supplier{id: $1}) " +
                        "SET n += { " +
                        "   name: $2, " +
                        "   contactName: $3, " +
                        "   contactTitle: $4, " +
                        "   address: $5, " +
                        "   city: $6, " +
                        "   region: $7," +
                        "   postalCode: $8, " +
                        "   country: $9, " +
                        "   phone: $10, " +
                        "   fax: $11, " +
                        "   homePage: $12 " +
                        "}",
                (st, p) -> {
                    st.setInt(1, p.supplierId);
                    st.setString(2, p.companyName);
                    st.setString(3, p.contactName);
                    st.setString(4, p.contactTitle);
                    st.setString(5, p.address);
                    st.setString(6, p.city);
                    st.setString(7, p.region);
                    st.setString(8, p.postalCode);
                    st.setString(9, p.country);
                    st.setString(10, p.phone);
                    st.setString(11, p.fax);
                    st.setString(12, p.homePage);
                });
    }


}
