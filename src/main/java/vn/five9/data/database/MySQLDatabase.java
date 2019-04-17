package vn.five9.data.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.config.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * provide connection pool
 */
public class MySQLDatabase {

    private static final Logger logger = LogManager.getLogger();
    private static MySQLDatabase instance = null;
    private BasicDataSource ds = new BasicDataSource();

    private void init(){
        ds.setDriverClassName(Config.repositoryDrive);
        ds.setUrl(Config.repositoryUrl);
        ds.setUsername(Config.repositoryUsername);
        ds.setPassword(Config.repositoryPassword);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    private MySQLDatabase() {
        init();
    }

    public static MySQLDatabase getInstance() {
        if(instance == null) {
            instance = new MySQLDatabase();
        }
        return instance;
    }

    /**
     *
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        }catch (SQLException e) {
            logger.error("Cannot get connection from datasource ", e.getMessage());
        }
        return conn;
    }
}
