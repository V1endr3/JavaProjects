package org.example.calcite;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalciteSqlTest {

    private SqlParserPos P;

    @BeforeEach
    public void setToDefault() {
        P = SqlParserPos.ZERO;
    }

    @Test
    public void testSelectWhere() {
        // 1. Table definition
        // 1.1. define table name
        SqlIdentifier tableIdentifier = new SqlIdentifier("table_name", P);
        // 1.2. define columns selected
        SqlNodeList selectList = new SqlNodeList(P);
        selectList.add(new SqlIdentifier("name", P));
        selectList.add(new SqlIdentifier("age", P));
        selectList.add(new SqlIdentifier("gender", P));
        selectList.add(new SqlIdentifier("tel", P));
        selectList.add(new SqlIdentifier("email", P));
        selectList.add(new SqlIdentifier("address", P));

        // 2. Where definition
        SqlOperator eq = SqlStdOperatorTable.EQUALS;
        SqlBasicCall whereCall = new SqlBasicCall(
                eq,
                new SqlNode[]{
                        new SqlIdentifier("name", P),
                        SqlLiteral.createCharString("张三", P)
                },
                P
        );

        SqlSelect select = new SqlSelect(
                P                           // Pos
                , null                      // KeywordList
                , selectList                // Selected Columns
                , tableIdentifier           // from
                , whereCall                 // where
                , null                      // groupBy
                , null                      // having
                , null                      // windowDecls
                , null                      // orderBy
                , null                      // offset
                , null                      // fetch
                , null                      // hints
        );

        String sql = select.toString();
        System.out.println("Generated SQL: \n" + sql);
    }

    @Test
    public void testJoin() {

    }

    @Test
    public void testUnionAll() {

    }

}
