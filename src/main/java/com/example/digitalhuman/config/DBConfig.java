package com.example.digitalhuman.config;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库配置 - H2内存数据库
 * 最简化配置，无需安装数据库，自动创建
 */
public class DBConfig {

    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        // 内存数据库，重启后数据清空
        dataSource.setUrl("jdbc:h2:mem:digital_human;DB_CLOSE_DELAY=-1;MODE=MySQL");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}
