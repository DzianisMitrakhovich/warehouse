package com.test.warehouse.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.containers.MySQLContainer;

import java.sql.SQLException;

abstract class AbstractDaoTest {

    static final MySQLContainer<?> MY_SQL_CONTAINER;
    static DriverManagerDataSource dataSource;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8")
                .withDatabaseName("warehouse");
        MY_SQL_CONTAINER.start();
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(MY_SQL_CONTAINER.getDriverClassName());
        dataSource.setUrl(MY_SQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(MY_SQL_CONTAINER.getUsername());
        dataSource.setPassword(MY_SQL_CONTAINER.getPassword());
    }

    @BeforeEach
    void setUp() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("test-schema.sql"));

    }

    @AfterEach
    void tearDown() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("drop-test-schema.sql"));
    }
}
