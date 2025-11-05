import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlinkTest {
    private static final int MAXIUM_ROWS = 50;

    private static final String TABLE_TEMPLATE = """
            CREATE TABLE {0} (
              {1}
            ) WITH (
              ''driver'' = ''org.postgresql.Driver'',
              ''connector'' = ''jdbc'',
              ''url'' = ''jdbc:postgresql://{2}:5432/sakila'',
              ''username'' = ''postgres'',
              ''password'' = ''{3}'',
              ''table-name'' = ''{4}''
            )
            """;

    private StreamExecutionEnvironment environment;
    private StreamTableEnvironment tableEnvironment;

    private String host;
    private String password;

    @BeforeEach
    public void init() {
        environment = StreamExecutionEnvironment.getExecutionEnvironment();
        tableEnvironment = StreamTableEnvironment.create(environment);
        host = "localhost";
        password = "123456";
    }

    @Test
    public void testUnionTableData() {
    }

    @Test
    public void testLeftJoin() {
    }

    @Test
    public void testRightJoin() {
    }

    @Test
    public void testFullJoin() {
    }

    @Test
    public void testInnerJoin() {
    }

    @Test
    public void testGroupBy() {
    }

    @Test
    public void testSqlFunction() {
    }

    @Test
    public void testWindowFunction() {
    }

    @Test
    public void testColRowTransform() {
    }

    @Test
    public void testExtractDataFromDataStream() throws Exception {
        tableEnvironment.executeSql(MessageFormat.format(TABLE_TEMPLATE,
                "payment_src",
                "`payment_id` int, `customer_id` int, `staff_id` int, `rental_id` int, `amount` decimal(5,2), `payment_date` timestamp",
                host,
                password,
                "payment"
        ));
        Table table = tableEnvironment.sqlQuery("select * from payment_src");
        List<List<Object>> dataList = extractTableRows(table);
        System.out.println(dataList);
        assert dataList.size() == MAXIUM_ROWS;
    }

    private List<List<Object>> extractTableRows(Table table) throws Exception {
        DataStream<Row> dataStream = tableEnvironment.toDataStream(table);
        List<Row> rows = dataStream.executeAndCollect(MAXIUM_ROWS);

        List<List<Object>> dataList = new ArrayList<>();
        // extract 1 sample data
        int idx = 0;
        int rowNumber = rows.size();
        Row row = rows.get(0);
        Set<String> columnNames = row.getFieldNames(true);
        assert columnNames != null;
        do {
            row = rows.get(idx);
            List<Object> rowData = new ArrayList<>();

            for (String columnName : columnNames) {
                Object columnValue = row.getField(columnName);
                if (columnValue instanceof LocalDateTime ldt) {
                    columnValue = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                rowData.add(columnValue);
            }
            dataList.add(rowData);
            idx++;
        } while (idx < rowNumber);
        return dataList;
    }
}
