package com.tms.config.dbcontext;

import java.sql.Connection;
import javax.sql.DataSource;

/**
 * Created by yzheng on 8/23/16.
 */

public interface JDBCDataSourceContext {
    DataSource getDataSource();
    Connection acquireSharedConnection();
    boolean hasSharedActiveConnection();
    void closeSharedConnection();
}
