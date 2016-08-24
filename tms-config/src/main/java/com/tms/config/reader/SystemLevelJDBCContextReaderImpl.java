package com.tms.config.reader;

import com.tms.config.dbcontext.JDBCContext;
import com.tms.config.dbcontext.impl.JDBCContextImpl;
import com.tms.tool.ObjectMapperSingleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzheng on 8/24/16.
 */

public class SystemLevelJDBCContextReaderImpl implements SystemLevelJDBCContextReader {

    private static String FLD_CONFIG_NAME = "configName";
    private static String FLD_CONFIG_VALUE = "configValue";

    private final File configFile;
    private final FileModifiedTimeStampTracker fileModifiedTimeStampTracker;
    private Long lastTS;
    private JDBCContext jdbcContext;

    public SystemLevelJDBCContextReaderImpl(File configFile, int checkPeriodInSecond) {
        this.configFile = configFile;
        this.fileModifiedTimeStampTracker = new FileModifiedTimeStampTracker(configFile, checkPeriodInSecond);
        this.lastTS = null;
    }

    protected void loadFileIfModified() {
        Long newTS = this.fileModifiedTimeStampTracker.getLastModifiedTime();
        if ((this.lastTS == null && newTS != null) ||
                (this.lastTS != null && newTS != null && this.lastTS.longValue() != newTS.longValue())) {
            synchronized (this.fileModifiedTimeStampTracker) {
                this.lastTS = newTS;
                Map<String, Map<String, Object>>  jdbcConfigMap = new HashMap<>();
                try (InputStream inputStream = new FileInputStream(this.configFile)) {
                    Map<String, Object> json = ObjectMapperSingleton.getInstance().readValue(inputStream, Map.class);
                    String configName = (String) json.get(FLD_CONFIG_NAME);
                    Map<String, Object> configValue = (Map) json.get(FLD_CONFIG_VALUE);
                    Map<String, Object> oldConfigValue = jdbcConfigMap.put(configName, configValue);
                    if (oldConfigValue != null) {
                        throw new IllegalStateException("Duplicate configuration entry under name = " + configName);
                    }
                } catch (FileNotFoundException fnfe) {
                    throw new RuntimeException(fnfe);
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }

                Map<String, Object> jdbcContextMap = jdbcConfigMap.get(PROP_JDBC_CONTEXT);
                if (jdbcConfigMap != null) {
                    Map<String, Map<String, Object> > namedDataSourcePropMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : jdbcContextMap.entrySet()) {
                        namedDataSourcePropMap.put(entry.getKey(), (Map<String, Object>) entry.getValue());
                    }
                    this.jdbcContext = JDBCContextImpl.createJDBCContext(namedDataSourcePropMap);

                } else {
                    this.jdbcContext = null;
                }
            }
        }
    }

    @Override
    public JDBCContext getJDBCContext() {
        this.loadFileIfModified();
        return this.jdbcContext;
    }
}