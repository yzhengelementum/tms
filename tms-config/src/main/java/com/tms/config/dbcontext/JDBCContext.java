package com.tms.config.dbcontext;

/**
 * Created by yzheng on 8/23/16.
 */

public interface JDBCContext {
    String PROP_DRIVER_CLASSNAME = "jdbc.driverClassName";
    String PROP_URL = "jdbc.url";
    String PROP_USERNAME = "jdbc.username";
    String PROP_PASSWORD = "jdbc.password";
    String PROP_MAX_ACTIVE = "jdbc.maxActive";
    String PROP_MAX_IDLE = "jdbc.maxIdle";
    String PROP_MAX_WAIT = "jdbc.maxWait";
    String PROP_TEST_ON_BORROW = "jdbc.testOnBorrow";
    String PROP_TEST_ON_RETURN = "jdbc.testOnReturn";
    String PROP_TEST_WHILE_IDLE = "jdbc.testWhileIdle";
    String PROP_VALIDATION_QUERY = "jdbc.validationQuery";
    String PROP_VALIDATION_QUERY_TIMEOUT = "jdbc.validationQueryTimeout";

    JDBCDataSourceContext getDefaultJDBCDataSourceContext();

    JDBCDataSourceContext getJDBCDataSourceContext(String profileName);

    void useJDBCProfileAsDefault(String profileName);

    void closeAllSharedConnections();
}
