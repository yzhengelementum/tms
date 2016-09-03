package com.tms.relational.clause.upsert;

import com.tms.relational.common.PreparedStatementFragment;
import com.tms.tool.ObjectMapperSingleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yzheng on 9/2/16.
 */

public class InsertClause {

    private String tableName;
    private Map<String, Object> insertKeyValuePairs;

    private InsertClause(String tableName, Map<String, Object> insertKeyValuePairs) {
        this.tableName = tableName;
        this.insertKeyValuePairs = insertKeyValuePairs;
    }

    private PreparedStatementFragment preparedStatementFragment;

    public PreparedStatementFragment getPreparedStatementFragment() {
        if (tableName == null || tableName.length() == 0) {
            throw new IllegalArgumentException("need to specify a table to insert a record.");
        }

        if (this.preparedStatementFragment == null && this.insertKeyValuePairs != null) {
            if (this.insertKeyValuePairs.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("INSERT INTO " + tableName + "(");
                List<Object> valueList = new ArrayList<>();
                StringBuilder questionMarks = new StringBuilder();
                for (Map.Entry<String, Object> entry : this.insertKeyValuePairs.entrySet()) {
                    if (questionMarks.length() > 0) {
                        questionMarks.append(", ");
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(entry.getKey());
                    questionMarks.append("?");
                    valueList.add(entry.getValue());
                }

                stringBuilder.append(") VALUES (" + questionMarks.toString() + ")");
                this.preparedStatementFragment = new PreparedStatementFragment(valueList, stringBuilder.toString());
            }
        }

        return this.preparedStatementFragment;
    }

    public static class Builder {
        private String tableName;
        private Map<String, Object> insertKeyValuePairs;

        public Builder insertToTable(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder insertValues(Map<String, Object> insertKeyValuePairs) {
            this.insertKeyValuePairs = insertKeyValuePairs;
            return this;
        }

        public Builder insertPOJO(Object pojo) {
            return this.insertValues(ObjectMapperSingleton.getInstance().convertValue(pojo, Map.class));
        }

        public InsertClause build() {
            return new InsertClause(tableName, insertKeyValuePairs);
        }
    }
}