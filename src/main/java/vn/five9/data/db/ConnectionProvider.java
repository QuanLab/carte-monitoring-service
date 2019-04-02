package vn.five9.data.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.five9.data.config.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * provide connection pool
 */
public class ConnectionProvider {

    private static final Logger logger = LogManager.getLogger();
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName(Config.repositoryDrive);
        ds.setUrl(Config.repositoryUrl);
        ds.setUsername(Config.repositoryUsername);
        ds.setPassword(Config.repositoryPassword);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    private ConnectionProvider() {

    }

    /**
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        }catch (SQLException e) {
            logger.error("Cannot get connection from datasource ", e.getMessage());
        }
        return conn;
    }
}
