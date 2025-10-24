package org.example.flink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

public class Application {
    public static void main(String[] args) {
        final EnvironmentSettings settings =
                EnvironmentSettings.newInstance().inBatchMode()
                        .build();
        final TableEnvironment env = TableEnvironment.create(settings);

        env.executeSql("""
                CREATE TABLE payment_src (
                  `payment_id` int,
                  `customer_id` int,
                  `staff_id` int,
                  `rental_id` int,
                  `amount` decimal(5,2),
                  `payment_date` timestamp
                ) WITH (
                  'driver' = 'org.postgresql.Driver',
                  'connector' = 'jdbc',
                  'url' = 'jdbc:postgresql://localhost:5432/sakila',
                  'username' = 'postgres',
                  'password' = 'pwd',
                  'table-name' = 'payment'
                )
                """);

        env.executeSql("""
                CREATE TABLE payment_dst (
                  `payment_id` int,
                  `customer_id` int,
                  `staff_id` int,
                  `rental_id` int,
                  `amount` decimal(5,2),
                  `payment_date` timestamp
                ) WITH (
                  'driver' = 'org.postgresql.Driver',
                  'connector' = 'jdbc',
                  'url' = 'jdbc:postgresql://localhost:5432/sakila',
                  'username' = 'postgres',
                  'password' = 'pwd',
                  'table-name' = 'payment_p2007_01'
                )
                """);

        Table tmpTable = env.sqlQuery("select * from payment_src order by rand() limit 10");
        env.createTemporaryView("payment_tmp", tmpTable);

        tmpTable.insertInto("payment_dst").execute();
    }
}
