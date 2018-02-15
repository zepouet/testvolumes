package org.zepouet.legacy;

import static org.rnorth.visibleassertions.VisibleAssertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;

public class EasiestMysqlTest {

    private static final Logger logger = LoggerFactory.getLogger(EasiestMysqlTest.class);

    @ClassRule
    public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.5");

    @Test
    public void testSimple() throws SQLException {
        ResultSet resultSet = performQuery(mySQLContainer, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);

        assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
    }

    protected ResultSet performQuery(MySQLContainer containerRule, String sql) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(containerRule.getJdbcUrl());
        hikariConfig.setUsername(containerRule.getUsername());
        hikariConfig.setPassword(containerRule.getPassword());

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }

}
