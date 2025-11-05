package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anyline.data.param.AggregationBuilder;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.TableBuilder;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.entity.AggregationConfig;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Table;
import org.anyline.metadata.type.DatabaseType;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
                    "database": "onebase_cloud_v3",
                    "username": "postgres",
                    "password": "123456"
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
        Long roleId = 6943244133695489L;
        ConfigStore cs = new DefaultConfigStore();
        cs.param("roleId", roleId);
        cs.page(1, 50);

        RunPrepare userPrepare = TableBuilder.init("app_auth_role_user AS aaru")
                .inner("system_users AS u",
                        "aaru.user_id = u.id",
                        "aaru.deleted = 0",
                        "u.deleted = 0")
                .select("aaru.id", "aaru.role_id AS member_id", "u.nickname as member_name", "'user' as member_type", "aaru.update_time")
                .build();
        RunPrepare deptPrepare = TableBuilder.init("app_auth_role_dept AS aard")
                .inner("system_dept AS d",
                        "aard.dept_id = d.id", "aard.deleted = 0", "d.deleted = 0")
                .select("aard.id", "aard.dept_id AS member_id", "d.name as member_name", "'dept' as member_type", "aard.update_time")
                .build();
        RunPrepare unioned = userPrepare.unionAll(deptPrepare);

        DataSet querys = temporary.querys(unioned, cs);
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
