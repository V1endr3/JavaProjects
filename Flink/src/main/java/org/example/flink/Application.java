package org.example.flink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Application {
    public static void main(String[] args) throws Exception {
        final int MAXIUM_ROWS = 10;
        // initialize flink environment with streaming execution env;
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        final StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(environment);
        // create test table;
        tableEnvironment.executeSql("""
                CREATE TABLE payment_src (
                  `payment_id` int, `customer_id` int, `staff_id` int, `rental_id` int, `amount` decimal(5,2), `payment_date` timestamp
                ) WITH (
                  'driver' = 'org.postgresql.Driver',
                  'connector' = 'jdbc',
                  'url' = 'jdbc:postgresql://localhost:5432/sakila',
                  'username' = 'postgres',
                  'password' = 'replacepwd',
                  'table-name' = 'payment'
                )
                """);
        // select sample data from test table, limit 10;
        Table tmpTable = tableEnvironment.sqlQuery("select * from payment_src");
        tableEnvironment.createTemporaryView("payment_tmp", tmpTable);

        // tranform TableAPI to DataStreamAPI;
        DataStream<Row> dataStream = tableEnvironment.toDataStream(tmpTable);
        // extract data from DataStream<?>
        List<Row> rows = dataStream.executeAndCollect(MAXIUM_ROWS);

        List<List<String>> dataList = extractDataList(rows);

        System.out.println(dataList);
        System.out.println("DONE");
    }

    private static List<List<String>> extractDataList(List<Row> rows) {
        List<List<String>> dataList = new ArrayList<>();
        // extract 1 sample data
        int idx = 0;
        int rowNumber = rows.size();
        Row row = rows.get(0);
        Set<String> columnNames = row.getFieldNames(true);
        do {
            row = rows.get(idx);
            List<String> rowData = new ArrayList<>();
            for (String columnName : columnNames) {
                Object columnValue = row.getField(columnName);
                rowData.add(String.valueOf(columnValue));
            }
            dataList.add(rowData);
            idx++;
        } while (idx < rowNumber);
        return dataList;
    }
}
