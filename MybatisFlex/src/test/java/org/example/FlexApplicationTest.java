package org.example;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.SystemUsers;
import org.example.mapper.SystemUsersMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FlexApplicationTest {

    @Autowired
    private SystemUsersMapper systemUsersMapper;

    @Test
    public void testInsert() {

        SystemUsers systemUsers = new SystemUsers();
        systemUsers.setUsername("hello");
        systemUsers.setStatus(0);

        int inserted = systemUsersMapper.insert(systemUsers);
        System.out.println(inserted);

    }

    @Test
    public void testQuery() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .eq(SystemUsers::getUsername, "admin");

        List<SystemUsers> systemUsers = TenantManager.withoutTenantCondition(() -> systemUsersMapper.selectListByQuery(queryWrapper));
        System.out.println(systemUsers);

    }

    @Test
    public void testQueryConditions() {
        QueryColumn ID = new QueryColumn("id");
        QueryColumn NAME = new QueryColumn("name");
        QueryColumn MOBILE = new QueryColumn("mobile");
        QueryColumn EMAIL = new QueryColumn("email");
        Long myId = 0L;
        String myName = "admin";
        String myMobile = "123";
        String myEmail = "abc@123.com";
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(ID.eq(myId).and(
                        NAME.eq(myName).when(StringUtils.isNotBlank(myName))
                                .or(MOBILE.eq(myMobile)).when(StringUtils.isNotBlank(myMobile))
                                .or(EMAIL.eq(myEmail).when(StringUtils.isNotBlank(myEmail)))
                ));
        System.out.println(queryWrapper.toSQL());
    }

    @Test
    public void testSubQuery() {
        String sql = QueryWrapper.create()
                .select(new QueryColumn("t", "id"))
                .from(
                        QueryWrapper.create()
                                .select("id", "name", "age")
                                .from("hello")
                ).as("t")
                .toSQL();
        System.out.println(sql);
    }

    @Test
    public void testComplexQueryTableExtractUsingCPI() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .eq(SystemUsers::getUsername, "hello");
        System.out.println(queryWrapper.toSQL());

        queryWrapper.eq(SystemUsers::getAdminType, 1);
        System.out.println(queryWrapper.toSQL());
    }

    @Test
    public void testAnnotationConstants() {
        QueryWrapper queryWrapper = QueryWrapper.create(SystemUsers.class)
                .select();

        String sql = queryWrapper.toSQL();
        System.out.println(sql);
    }
}
