package org.zepouet;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.ToStringConsumer;

public class SimpleMysqlWithVolumeTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMysqlWithVolumeTest.class);

    public static final String DATA_SNAPSHOT = "/Users/nicolas/software/testvolumes/data/"+ SystemUtils.OS_NAME;
    public static final String DATA_TEMP = "/Users/nicolas/docker-volumes/mysql-data-"+System.currentTimeMillis();

    @AfterClass
    public static void cleanResources() throws IOException {
        File destination = new File(DATA_TEMP);
        FileUtils.deleteDirectory(destination);
    }

    public static TestVolume testVolume = new TestVolume(DATA_SNAPSHOT, DATA_TEMP);
    public static MySQLContainer mySQLContainer = ((MySQLContainer) new MySQLContainer("mysql:5.5")
            .withLogConsumer(new ToStringConsumer()))
            .withUsername("johndoe")
            .withPassword("givemefive")
            .withDatabaseName("feedback");

    @ClassRule
    public static RuleChain ruleChain = RuleChain.outerRule(testVolume).around(mySQLContainer);

    static {
        mySQLContainer.addFileSystemBind(DATA_TEMP,
                "/var/lib/mysql",
                BindMode.READ_WRITE);
    }

    @Test
    public void testSimple() throws SQLException, IOException {
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

        statement = ds.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select SUMMARY from comments");
        resultSet.next();
        String nbRowCount = resultSet.getString(1);
        System.out.println(nbRowCount);
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
