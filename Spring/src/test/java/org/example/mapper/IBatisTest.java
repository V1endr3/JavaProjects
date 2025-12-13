package org.example.mapper;

import cn.hutool.core.util.XmlUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IBatisTest {

    @Test
    public void testIBatis() {
        String sql = """
                <script>
                select * from ods.DEVICE_DATA
                where 1=1
                <if test="starttime != null and endtime != null">
                    AND CREATE_AT BETWEEN #{starttime} AND #{endtime}
                </if>
                </script>
                """;

        Configuration configuration = new Configuration();
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, sql, null);

        Map<String, String> params = new HashMap<>();
        params.put("starttime", "hello");
        params.put("endtime", "world");

        BoundSql boundSql = sqlSource.getBoundSql(params);
        System.out.println(boundSql.getSql());
        System.out.println(boundSql.getParameterObject());
    }
}
