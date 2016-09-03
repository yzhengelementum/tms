package com.tms.relational.clause.upsert;

import com.tms.relational.clause.filter.DBQueryFilter;
import com.tms.relational.common.PreparedStatementFragment;
import com.tms.tool.ObjectMapperSingleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yzheng on 9/2/16.
 */

/**
 * update tableName
 * set
 *  col1 = value1,
 *  col2 = value2
 * where
 *  col3 = value3
 */

public class UpdateClause {
    private String tableName;
    private Map<String, Object> updatedKeyValue;
    private DBQueryFilter queryFilter;

    private UpdateClause(String tableName, Map<String, Object> updatedKeyValue, DBQueryFilter queryFilter) {
        if (this.tableName == null || this.tableName.length() == 0) {
            throw new IllegalArgumentException("No table is specified in the update clause");
        }

        if (this.updatedKeyValue == null || this.updatedKeyValue.size()  == 0) {
            throw new IllegalArgumentException("No updated key value are specified in update clause.");
        }

        this.tableName = tableName;
        this.updatedKeyValue = updatedKeyValue;
        this.queryFilter = queryFilter;
    }

    private PreparedStatementFragment preparedStatementFragment;
    public PreparedStatementFragment getPreparedStatementFragment() {
        if (this.preparedStatementFragment == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UPDATE " + this.tableName + " SET ");
            List<Object> valueList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : this.updatedKeyValue.entrySet()) {
                if (valueList.size() != 0) {
                    stringBuilder.append(", ");
                }

                stringBuilder.append(entry.getKey() + " = ?");
                valueList.add(entry.getValue());
            }

            stringBuilder.append(" WHERE " + queryFilter.getPreparedStatementFragment(tableName).getSqlString());
            valueList.addAll(this.queryFilter.getPreparedStatementFragment(tableName).getValueList());
            this.preparedStatementFragment = new PreparedStatementFragment(valueList, stringBuilder.toString());
        }

        return this.preparedStatementFragment;
    }

    public static class Builder {
        private String tableName;
        private Map<String, Object> updatedKeyValue;
        private DBQueryFilter queryFilter;

        public Builder updateTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder updatedKeyValue(Map<String, Object> updatedKeyValue) {
            this.updatedKeyValue = updatedKeyValue;
            return this;
        }

        public Builder updatedPojo(Object pojo) {
            return updatedKeyValue(ObjectMapperSingleton.getInstance().convertValue(pojo, Map.class));
        }

        public Builder addFilter(DBQueryFilter queryFilter) {
            this.queryFilter = queryFilter;
            return this;
        }

        public UpdateClause build() {
            return new UpdateClause(tableName, updatedKeyValue, queryFilter);
        }
    }
}