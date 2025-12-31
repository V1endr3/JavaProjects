package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.*;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

public class MetadataCollector {

    public static void main(String[] args) {
        // Database config
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("hello");
        hikariConfig.setConnectionTestQuery("SELECT 42");
        DataSource dataSource = new HikariDataSource(hikariConfig);

        DatabaseConnectionSource databaseConnectionSource = DatabaseConnectionSources.fromDataSource(dataSource);

        LoadOptions loadOptions = LoadOptionsBuilder.builder()
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.detailed())
                .build();

        LimitOptions limitOptions = LimitOptionsBuilder.builder()
                .includeAllTables()
                .build();

        SchemaCrawlerOptions schemaCrawlerOptions = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLoadOptions(loadOptions)
                .withLimitOptions(limitOptions);

        Catalog catalog = SchemaCrawlerUtility.getCatalog(databaseConnectionSource, schemaCrawlerOptions);

        Collection<Schema> schemas = catalog.getSchemas();

        schemas.forEach(schema -> {
            String catalogName = schema.getCatalogName();
            String schemaName = schema.getName();
            String schemaFullName = schema.getFullName();
            String schemaRemarks = schema.getRemarks();
            Map<String, Object> schemaAttributes = schema.getAttributes();

            String schemaInfomation = MessageFormat.format("EntityPath: {0} => {1}\n\t{2}: {3}\n\t{4}",
                    catalogName, schemaName,
                    schemaFullName, schemaRemarks,
                    schemaAttributes.toString()
            );
            System.out.println(schemaInfomation);
        });

        Collection<Table> tables = catalog.getTables();

        System.out.println("DONE");
    }

}
