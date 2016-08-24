package com.tms.config.reader;

import com.tms.config.dbcontext.JDBCContext;

/**
 * Created by yzheng on 8/24/16.
 */
public interface SystemLevelJDBCContextReader {

    String PROP_JDBC_CONTEXT = "jdbcContext";
    JDBCContext getJDBCContext();
}
