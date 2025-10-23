package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anyline.data.param.AggregationBuilder;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Aggregation;
import org.anyline.entity.AggregationConfig;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Catalog;
import org.anyline.metadata.Schema;
import org.anyline.metadata.Table;
import org.anyline.metadata.type.DatabaseType;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.postgresql.util.PGInterval;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class ApplicationMain {

    static AnylineService<?> temporary;

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(ApplicationMain.class);
        application.run(args);
        // 0.准备数据源信息
        String jdbcString = """
                {
                    "host": "localhost",
                    "port": 5432,
                    "database": "db",
                    "username": "postgres",
                    "password": "dbpassword"
                }
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        Properties properties = objectMapper.readValue(jdbcString, Properties.class);

        // 1. 解析数据库
        DatabaseType databaseType = parseDatabaseType("PostgreSQL");
        // 2. 初始化Driver
        String urlTemplate = databaseType.url(); // jdbc:postgresql://{host}:{port:5432}/{database}}
        ///由于Anyline对PostgreSQL系的JDBC URL拼写多了个'}',需要对其进行特殊判断
        if ("org.postgresql.Driver".equals(databaseType.driver())) {
            urlTemplate = "jdbc:postgresql://{host}:{port:5432}/{database}";
        }
        // 3. 拼装JDBC连接串
        StringBuffer sb = new StringBuffer();
        final String PARAM_PATTERN = "\\{([^{}:]+)(:[^{}]+)?\\}";
        Pattern pattern = Pattern.compile(PARAM_PATTERN);
        Matcher matcher = pattern.matcher(urlTemplate);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = String.valueOf(properties.get(key));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        String url = sb.toString();
        // 4. 构造一次性DataSource
        String username = (String) properties.get("username");
        String password = (String) properties.get("password");
        SingleConnectionDataSource datasource = new SingleConnectionDataSource(
                url,
                username,
                password,
                true
        );
        temporary = ServiceProxy.temporary(datasource);
        // 5. 测试连通性
        System.out.println(String.format("========================================="));
        boolean validity = temporary.validity();
        boolean hit = temporary.hit();
        if (validity || hit) {
            System.out.println("连接测试成功！");
        } else {
            System.out.println("连接测试失败");
        }
        // 6. 获取 Catalog => Schema => Table => Column
        Catalog catalog = temporary.metadata().catalog();
        Schema schema = temporary.metadata().schema();
        Table table = temporary.metadata().table("flow_execution_log");
//        Map<String, View> views = temporary.metadata().views();
//        System.out.println(String.format("SCHEMA:\t%s,\nCATALOG:\t%s,\nTABLE: \t%s", schema.getName(), catalog.getName(), table.getName()));

//        Map<String, Object> columns = table.getColumns();
//        for (String columnName : columns.keySet()) {
//            Column column = (Column) columns.get(columnName);
////            System.out.println("\t" + column.toString());
//        }
//        System.out.println(String.format("========================================="));
//        System.out.println("Done!");

        ConfigStore cs = new DefaultConfigStore();
        cs.ge("start_time", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
        cs.le("start_time", LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999));

        DataSet querys = temporary.aggregation(table)
                .configs(cs)
                .aggregation(Aggregation.SUM, "CASE execution_result WHEN 'success' THEN 1 ELSE 0 END", "success_count")
                .aggregation(Aggregation.SUM, "CASE execution_result WHEN 'failed' THEN 1 ELSE 0 END", "fail_count")
                .aggregation(Aggregation.AVG, "end_time - start_time", "avg_duration")
                .querys();

        List<DataRow> rows = querys.getRows();
        Object o = rows.get(0).get("avg_duration");

        int avgDuration = ((PGInterval) querys.getRows().get(0).get("avg_duration")).getMicroSeconds();
        System.exit(1);
    }

    public static DataRow aggregation(Table table,
                                      ConfigStore configs,
                                      AggregationConfig... aggConfigs) {
        AggregationBuilder aggBuilder = temporary.aggregation(table)
                .configs(configs);
        for (AggregationConfig aggConfig : aggConfigs) {
            aggBuilder = aggBuilder.aggregation(aggConfig.getAggregation(), aggConfig.getField(), aggConfig.getAlias());
        }
        return aggBuilder.query();

    }

    private static DatabaseType parseDatabaseType(String databaseType) {
        if (databaseType == null || databaseType.isBlank()) {
            throw new RuntimeException("数据库类型为空!");
        }
        DatabaseType parseType = null;
        for (DatabaseType dbType : DatabaseType.values()) {
            if (dbType.title().equalsIgnoreCase(databaseType)) {
                parseType = dbType;
                break;
            }
        }
        if (parseType == null) {
            throw new RuntimeException("数据库类型不正确");
        }
        if (parseType.driver() == null || parseType.driver().isBlank()) {
            throw new RuntimeException("不支持的数据库类型");
        }
        return parseType;
    }
}
