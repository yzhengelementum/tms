package com.tms.config.dbcontext.impl;

import com.tms.config.dbcontext.JDBCContext;
import com.tms.config.dbcontext.JDBCDataSourceContext;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

/**
 * Created by yzheng on 8/23/16.
 */

public class JDBCContextImpl implements JDBCContext {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCContextImpl.class);

    private static Map<Map<String, Object>, DataSource> dataSourceCacheMap = new ConcurrentHashMap<>();
    private final Map<String, JDBCDataSourceContext> profileDataSource;
    private JDBCDataSourceContext defaultJDBCDataSourceContext;
    private String defaultJDBCDataSourceProfileName;

    public JDBCContextImpl(Map<String, JDBCDataSourceContext> profileDataSource, String defaultJDBCDataSourceProfileName) {
        Map<String, JDBCDataSourceContext> m = new HashMap<>();
        m.putAll(profileDataSource);
        this.profileDataSource = Collections.unmodifiableMap(m);
        this.defaultJDBCDataSourceProfileName = defaultJDBCDataSourceProfileName;
    }

    public JDBCContextImpl(Map<String, JDBCDataSourceContext> profileDataSource) {
        this(profileDataSource, null);
    }

    @Override
    public JDBCDataSourceContext getDefaultJDBCDataSourceContext() {
        return this.defaultJDBCDataSourceContext;
    }

    @Override
    public JDBCDataSourceContext getJDBCDataSourceContext(String profileName) {
        return this.profileDataSource.get(profileName);
    }

    @Override
    public void useJDBCProfileAsDefault(String profileName) {
        if (profileName == null) {
            this.defaultJDBCDataSourceContext = null;
            this.defaultJDBCDataSourceProfileName = null;
        } else {
            JDBCDataSourceContext jdbcDataSourceContext = this.profileDataSource.get(profileName);
            if (jdbcDataSourceContext != null) {
                this.defaultJDBCDataSourceContext = jdbcDataSourceContext;
                this.defaultJDBCDataSourceProfileName = profileName;
            } else {
                throw new IllegalArgumentException("Not found the JDBC Context for profile = " + profileName);
            }
        }
    }

    @Override
    public void closeAllSharedConnections() {
        for (JDBCDataSourceContext jdbcDataSourceContext : this.profileDataSource.values()) {
            try {
                jdbcDataSourceContext.closeSharedConnection();
            } catch (RuntimeException e) {
                LOG.error("Unable to close the shared connection.", e);
            }
        }
    }

    public static JDBCContext createJDBCContext(Map<String, Map<String, Object>> namedDataSourcePropMap) {
        Map<String, JDBCDataSourceContext> m = new HashMap<>();
        for (Map.Entry<String, Map<String, Object> > entry : namedDataSourcePropMap.entrySet()) {
            m.put(entry.getKey(), new JDBCDataSourceContextImpl(getDataSourceFromPropMap(entry.getValue())));
        }

        return new JDBCContextImpl(m);
    }

    public static DataSource getDataSourceFromPropMap(Map<String, Object> dataSourcePropMap) {
        DataSource ds = dataSourceCacheMap.get(dataSourcePropMap);
        if (ds == null) {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName((String) dataSourcePropMap.get(PROP_DRIVER_CLASSNAME));
            dataSource.setUrl((String) dataSourcePropMap.get(PROP_URL));
            dataSource.setUsername((String) dataSourcePropMap.get(PROP_USERNAME));
            dataSource.setPassword((String) dataSourcePropMap.get(PROP_PASSWORD));

            if (dataSourcePropMap.get(PROP_MAX_WAIT) != null) {
                dataSource.setMaxWait(((Number) dataSourcePropMap.get(PROP_MAX_WAIT)).longValue());
            }

            if (dataSourcePropMap.get(PROP_MAX_ACTIVE) != null) {
                dataSource.setMaxActive(((Number)dataSourcePropMap.get(PROP_MAX_ACTIVE)).intValue());
            }
            if (dataSourcePropMap.get(PROP_MAX_IDLE) != null) {
                dataSource.setMaxIdle(((Number)dataSourcePropMap.get(PROP_MAX_IDLE)).intValue());
            }

            if (dataSourcePropMap.get(PROP_VALIDATION_QUERY) != null) {
                dataSource.setValidationQuery((String) dataSourcePropMap.get(PROP_VALIDATION_QUERY));
            }

            if (dataSourcePropMap.get(PROP_VALIDATION_QUERY_TIMEOUT) != null) {
                dataSource.setValidationQueryTimeout(((Number) dataSourcePropMap.get(PROP_VALIDATION_QUERY_TIMEOUT)).intValue());
            }

            if (dataSourcePropMap.get(PROP_TEST_ON_BORROW) != null) {
                dataSource.setTestOnBorrow((Boolean)dataSourcePropMap.get(PROP_TEST_ON_BORROW));
            }

            if (dataSourcePropMap.get(PROP_TEST_ON_RETURN) != null) {
                dataSource.setTestOnReturn((Boolean)dataSourcePropMap.get(PROP_TEST_ON_RETURN));
            }

            if (dataSourcePropMap.get(PROP_TEST_WHILE_IDLE) != null) {
                dataSource.setTestWhileIdle((Boolean) dataSourcePropMap.get(PROP_TEST_WHILE_IDLE));
            }

            ds = dataSource;
            DataSource oldDS = dataSourceCacheMap.putIfAbsent(dataSourcePropMap, ds);
            if (oldDS != null) {
                ds = oldDS;
            }
        }
        return ds;
    }
}