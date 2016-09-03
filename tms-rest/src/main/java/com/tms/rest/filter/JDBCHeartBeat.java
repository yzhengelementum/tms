package com.tms.rest.filter;

import com.tms.config.dbcontext.JDBCContext;
import com.tms.config.dbcontext.impl.JDBCContextThreadLocalImpl;
import com.tms.config.reader.SystemLevelJDBCContextReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yzheng on 8/28/16.
 */

public class JDBCHeartBeat {
    private static final Logger LOG = LoggerFactory.getLogger(JDBCHeartBeat.class);

    private SystemLevelJDBCContextReader jdbcContextReader;
    private long pollPeriod;
    private boolean continuePoll = true;

    public JDBCHeartBeat(long pollPeriod, SystemLevelJDBCContextReader jdbcContextReader) {
        this.pollPeriod = pollPeriod;
        this.jdbcContextReader = jdbcContextReader;
    }

    public void start() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (continuePoll) {
                    try {
                        Thread.sleep(pollPeriod);
                    } catch (InterruptedException ie) {
                        throw new RuntimeException(ie);
                    }
                    processConfFile();
                }
            }
        };

        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.start();
    }

    public void shutdown() { this.continuePoll = false; }

    public void processConfFile() {
        JDBCContext jdbcContext = this.jdbcContextReader.getJDBCContext();

        try {
            JDBCContextThreadLocalImpl.INSTANCE.set(jdbcContext);
            this.checkJDBC();
        } finally {
            JDBCContext context = JDBCContextThreadLocalImpl.INSTANCE.get();
            JDBCContextThreadLocalImpl.INSTANCE.clear();

            if (context != null) {
                context.closeAllSharedConnections();
            }
        }
    }

    public void checkJDBC() {
        try (Connection conn = JDBCContextThreadLocalImpl.INSTANCE.get().getDefaultJDBCDataSourceContext()
                .acquireSharedConnection()){

            final String CURRENT_TIME = "select CURRENT_TIME();";
            PreparedStatement ps = conn.prepareStatement(CURRENT_TIME);
            ps.executeQuery();

            LOG.info("JDBCHeartBeat.checkJDBC successful.");

        } catch (SQLException sqle) {

            LOG.error("JdbcHeartBeat.checkJDBC: SQLException = " + sqle);
        }
    }
}