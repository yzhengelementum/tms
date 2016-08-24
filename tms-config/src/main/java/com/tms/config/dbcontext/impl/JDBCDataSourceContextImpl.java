package com.tms.config.dbcontext.impl;

import com.tms.config.dbcontext.JDBCDataSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Created by yzheng on 8/23/16.
 */

public class JDBCDataSourceContextImpl implements JDBCDataSourceContext {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCDataSourceContextImpl.class);

    private final DataSource dataSource;

    private static final ThreadLocal<Connection> SHARED_CONN = new ThreadLocal<>();

    public JDBCDataSourceContextImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public Connection acquireSharedConnection() {
        Connection sharedConn = SHARED_CONN.get();
        if (sharedConn != null) {
            try {
                if (sharedConn.isClosed()) {
                    SHARED_CONN.remove();
                    sharedConn = this.dataSource.getConnection();
                    sharedConn.setAutoCommit(false);
                    SHARED_CONN.set(sharedConn);
                }
            } catch (SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        } else {
            try {
                sharedConn = this.dataSource.getConnection();
                sharedConn.setAutoCommit(false);
                SHARED_CONN.set(sharedConn);
            } catch (SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        }

        return sharedConn;
    }

    @Override
    public boolean hasSharedActiveConnection() {
        Connection sharedConn = SHARED_CONN.get();
        try {
            return sharedConn == null ? false : !sharedConn.isClosed();
        } catch (SQLException sqle) {
            LOG.error("hasSharedActiveConnection() throws SQLException.", sqle);
            throw new RuntimeException(sqle);
        }
    }

    @Override
    public void closeSharedConnection() {
        Connection sharedConn = SHARED_CONN.get();
        if (sharedConn != null) {
            try {
                if (!sharedConn.isClosed()) {
                    sharedConn.close();
                }
            } catch (SQLException sqle) {
                LOG.error("closeSharedConnection() throws SQLException.", sqle);
            } finally {
                SHARED_CONN.remove();
            }
        }
    }
}