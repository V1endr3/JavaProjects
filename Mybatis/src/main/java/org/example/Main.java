package org.example;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String sqlTemplate = "select * from tbl where 1=1 <if test='id != null and id.length > 0'> and id = #{id}</if>";
        Configuration configuration = new Configuration(null);
        String sqlCdata = "<script><![CDATA[" + sqlTemplate + "]]></script>";
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, sqlCdata, Map.class);

        var params = new HashMap<String, Object>();
        params.put("id", "123");
        BoundSql boundSql = sqlSource.getBoundSql(params);
        System.out.println(boundSql.getSql());
    }
}