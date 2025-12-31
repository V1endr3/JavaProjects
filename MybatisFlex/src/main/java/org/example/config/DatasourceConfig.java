//package org.example.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class DatasourceConfig {
//    @Bean
//    public HikariDataSource hikariDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("com.kingbase8.Driver");
//        config.setJdbcUrl("jdbc:kingbase8://10.11.135.7:15432/cloud_v3");
//        config.setUsername("system");
//        config.setPassword("3er4#ER$");
//
//        HikariDataSource datasource = new HikariDataSource(config);
//        return datasource;
//    }
//
//}
