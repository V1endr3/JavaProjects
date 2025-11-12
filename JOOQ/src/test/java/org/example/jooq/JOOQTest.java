package org.example.jooq;

import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.jooq.impl.DSL.*;

public class JOOQTest {

    private DSLContext dsl;

    @BeforeEach
    public void init() {
        dsl = using(SQLDialect.DEFAULT);
    }

    @Test
    public void testSelect() {
        String sql = dsl
                .select(
                        field("user_id", String.class).as("id"),
                        field("user_name").as("name"),
                        upper(field("user_name", String.class)).as("UPPER_NAME"),
                        case_(field("gender")).when(value("male"), 1).else_(0).as("casewhen_"),
                        function("JSON_EXISTS", Boolean.class, field("inputJson"), value("jsonPath"))
                ).from(table("table1").as("t1"))
                .getSQL(ParamType.INLINED);

        System.out.println(sql);
    }


    @Test
    public void testWhere() {
        String sql = dsl
                .select(
                        field("user_id").as("id")
                )
                .from(table("sys_user").as("u"))
                .where(
                        or(
                                field("user_id").isNotNull(),
                                field("user_name").isNotNull()
                        ).and(
                                field("user_name").eq("admin")
                        )
                )
                .getSQL(ParamType.INLINED);

        System.out.println(sql);
    }

    @Test
    public void testSubQuery() {
        String sql = dsl
                .select()
                .from(select().from("table1").asTable("t1"))
                .getSQL(ParamType.INLINED);

        System.out.println(sql);
    }

    @Test
    public void testUnion() {
        String sql = dsl
                .select()
                .from(
                        select().from("table1")
                                .unionAll(select().from("table2"))
                )
                .getSQL(ParamType.INLINED);

        System.out.println(sql);
    }

    @Test
    public void testComplexSql() {
        String PARAM_REPLACEMENT = "123123123";

        String sql = dsl
                .select(
                )
                .from(
                        select(
                                field(name("aaru", "id")),
                                field(name("aaru", "role_id")).as("member_id"),
                                field(name("aaru", "update_time")),
                                value(-1).as("is_include_child"),
                                field(name("d", "name")).as("dept_name"),
                                field(name("u", "nickname")).as("member_name"),
                                value("user").as("member_type")
                        )
                                .from(
                                        table("app_auth_role_user").asTable("aaru"),
                                        table("system_user").asTable("u"),
                                        table("system_dept").asTable("d")
                                )
                                .where(
                                        and(
                                                field(name("aaru", "user_id")).eq(field(name("u", "id"))),
                                                field(name("u", "dept_id")).eq(field(name("d", "id"))),
                                                field(name("aaru", "role_id")).eq(PARAM_REPLACEMENT),
                                                field(name("aaru", "deleted")).eq(value(0)),
                                                field(name("u", "nickname")).like("%" + PARAM_REPLACEMENT + "%"),
                                                field(name("u", "deleted")).eq(value(0)),
                                                field(name("d", "deleted")).eq(value(0))
                                        )
                                )
                                .unionAll(
                                        select(
                                                field(name("aard", "id")),
                                                field(name("aard", "dept_id")).as("member_id"),
                                                field(name("aard", "update_time")),
                                                field(name("aard", "is_include_child"), Integer.class),
                                                field(name("d", "name")).as("dept_name"),
                                                field(name("d", "name")).as("member_name"),
                                                value("dept").as("member_type")
                                        )
                                                .from(
                                                        table("app_auth_role_dept").asTable("aard"),
                                                        table("system_dept").asTable("d")
                                                )
                                                .where(
                                                        and(
                                                                field(name("aard", "dept_id")).eq(field(name("d", "id"))),
                                                                field(name("aard", "role_id")).eq(PARAM_REPLACEMENT),
                                                                field(name("d", "name")).like("%" + PARAM_REPLACEMENT + "%"),
                                                                field(name("aard", "deleted")).eq(value(0)),
                                                                field(name("d", "deleted")).eq(value(0))
                                                        )
                                                ))
                                .asTable("combined_table")
                )
                .getSQL(ParamType.NAMED_OR_INLINED);

        System.out.println(sql);
    }


    @Test
    public void test1() {
        var fromSQL = dsl
                .select(
                        field("user_id", String.class).as("id"),
                        field("user_name", String.class).as("name")
                ).from(table("table1").as("t1"))
                .$where(
                        and(
                                field("user_id").ge(0),
                                field("user_name").likeIgnoreCase("%abc%")

                        ));

        Map<String, Param<?>> params = fromSQL.getParams();
        System.out.println(fromSQL.getBindValues());
        String sql = fromSQL.getSQL(ParamType.INDEXED);
        System.out.println(sql);
    }
}
