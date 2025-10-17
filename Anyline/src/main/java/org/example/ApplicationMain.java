package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anyline.metadata.Catalog;
import org.anyline.metadata.Column;
import org.anyline.metadata.Schema;
import org.anyline.metadata.Table;
import org.anyline.metadata.type.DatabaseType;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class ApplicationMain {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(ApplicationMain.class);
        application.run(args);
        // 0.准备数据源信息
        String jdbcString = """
                {
                    "host": "1.1.1.1",
                    "port": 5432,
                    "database": "",
                    "username": "postgres",
                    "password": ""
                }
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        Properties properties = objectMapper.readValue(jdbcString, Properties.class);

        // 1. 解析数据库
        DatabaseType databaseType = parseDatabaseType("PostgreSQL");
        // 2. 初始化Driver
        String driverName = databaseType.driver();
        Class<? extends Driver> driverClass = (Class<? extends Driver>) Class.forName(driverName);
        Driver driver = driverClass.getDeclaredConstructor().newInstance();
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
        SimpleDriverDataSource datasource = new SimpleDriverDataSource(
                driver,
                url,
                username,
                password
        );
        AnylineService<?> temporary = ServiceProxy.temporary(datasource);
        // 5. 测试连通性
        System.out.println(String.format("========================================="));
        boolean validity = temporary.validity();
        boolean hit = temporary.hit();
        if (validity && hit) {
            System.out.println("连接测试成功！");
        } else {
            System.out.println("连接测试失败");
        }
        // 6. 获取 Schema => Catalog => Table => Column
        Schema schema = temporary.metadata().schema();
        Catalog catalog = temporary.metadata().catalog();
        Table table = temporary.metadata().table("metadata_datasource");
        System.out.println(String.format("SCHEMA:\t%s,\nCATALOG:\t%s,\nTABLE: \t%s", schema.getName(), catalog.getName(), table.getName()));

        Map<String, Object> columns = table.getColumns();
        for (String columnName : columns.keySet()) {
            Column column = (Column) columns.get(columnName);
            System.out.println("\t" + column.toString());
        }
        System.out.println(String.format("========================================="));
        System.out.println("Done!");
        System.exit(0);
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
