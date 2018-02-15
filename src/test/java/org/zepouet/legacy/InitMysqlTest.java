package org.zepouet.legacy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.zepouet.TestVolume;

public class InitMysqlTest {

    private static final Logger logger = LoggerFactory.getLogger(InitMysqlTest.class);

    public static final String DATA_SNAPSHOT = "/Users/nicolas/docker-volumes/mysql-data-snapshot";

    @AfterClass
    public static void cleanResources() throws IOException {
        File destination = new File(DATA_SNAPSHOT);
        FileUtils.deleteDirectory(destination);
    }

    @ClassRule
    public static MySQLContainer mySQLContainer = ((MySQLContainer) new MySQLContainer("mysql:5.5")
            .withLogConsumer(new Slf4jLogConsumer(logger)))
            .withUsername("johndoe")
            .withPassword("givemefive")
            .withDatabaseName("feedback");

    static {
        mySQLContainer.addFileSystemBind(DATA_SNAPSHOT,
                "/var/lib/mysql",
                BindMode.READ_WRITE);
    }

    @Test
    public void testSimple() throws SQLException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream is = classLoader.getResourceAsStream("creation.sql");
        String content = IOUtils.toString(is, "UTF-8");
        performCreation(mySQLContainer, content);

        is = classLoader.getResourceAsStream("insertion.sql");
        content = IOUtils.toString(is, "UTF-8");
        performInsertion(mySQLContainer, content);

        performDisplay(mySQLContainer);
    }

    protected void performCreation(MySQLContainer containerRule, String sql) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(containerRule.getJdbcUrl());
        hikariConfig.setUsername(containerRule.getUsername());
        hikariConfig.setPassword(containerRule.getPassword());

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.executeUpdate(sql);
    }

    protected void performInsertion(MySQLContainer containerRule, String sql) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(containerRule.getJdbcUrl());
        hikariConfig.setUsername(containerRule.getUsername());
        hikariConfig.setPassword(containerRule.getPassword());

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    protected void performDisplay(MySQLContainer containerRule) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(containerRule.getJdbcUrl());
        hikariConfig.setUsername(containerRule.getUsername());
        hikariConfig.setPassword(containerRule.getPassword());

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement = ds.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select SUMMARY from comments");
        resultSet.next();
        String nbRowCount = resultSet.getString(1);
        System.out.println(nbRowCount);
    }

}
