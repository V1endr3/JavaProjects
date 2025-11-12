package org.example.calcite;

import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.util.SqlBuilder;
import org.apache.calcite.tools.RelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalciteRelTest {

    private RelBuilder builder;
    private RelToSqlConverter converter;
    private SqlBuilder sqlBuilder;

    @BeforeEach
    public void init() {
        RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
    }

    @Test
    public void testSelect() {
    }
}
