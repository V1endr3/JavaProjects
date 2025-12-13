package org.example;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import org.example.entity.SystemUsers;
import org.example.mapper.SystemUsersMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
                .eq(SystemUsers::getUsername, "hello");

        List<SystemUsers> systemUsers = systemUsersMapper.selectListByQuery(queryWrapper);
        System.out.println(systemUsers);

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
}
