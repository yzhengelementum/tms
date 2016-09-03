package com.tms.rest.filter;

import com.tms.config.dbcontext.JDBCContext;
import com.tms.config.dbcontext.impl.JDBCContextThreadLocalImpl;
import com.tms.config.reader.SystemLevelJDBCContextReader;
import com.tms.config.reader.SystemLevelJDBCContextReaderImpl;
import java.io.File;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by yzheng on 8/24/16.
 */

public class JDBCContextFilter implements Filter{
    private JDBCHeartBeat jdbcHeartBeat;
    private SystemLevelJDBCContextReader reader;
    private File confFile;

    public JDBCContextFilter() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.confFile = new File("/etc/tms/conf/jdbc.json");
        this.reader = new SystemLevelJDBCContextReaderImpl(this.confFile, 60);
        this.jdbcHeartBeat = new JDBCHeartBeat(5000, reader);
        this.jdbcHeartBeat.start();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        JDBCContext jdbcContext = this.reader.getJDBCContext();
        jdbcContext.useJDBCProfileAsDefault("tms");

        try {
            JDBCContextThreadLocalImpl.INSTANCE.set(jdbcContext);
            chain.doFilter(request, response);
        } finally {

            JDBCContext context = JDBCContextThreadLocalImpl.INSTANCE.get();
            JDBCContextThreadLocalImpl.INSTANCE.clear();
            if (context != null) {
                context.closeAllSharedConnections();
            }
        }
    }

    @Override
    public void destroy() { this.jdbcHeartBeat.shutdown(); }
}