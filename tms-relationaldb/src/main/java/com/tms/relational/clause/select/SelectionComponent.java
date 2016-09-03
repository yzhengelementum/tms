package com.tms.relational.clause.select;

import javax.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Created by yzheng on 9/2/16.
 */

@Getter
public class SelectionComponent {
    @NotNull
    private String columnName;
    private AggregateFunction aggregateFunction;
    private As as;

    public SelectionComponent(String columnName) {
        this(columnName, null, null);
    }

    public SelectionComponent(String columnName, AggregateFunction aggregateFunction) {
        this(columnName, aggregateFunction, null);
    }

    public SelectionComponent(String columnName, AggregateFunction aggregateFunction, As as) {
        if (columnName == null || columnName.length() == 0) {
            throw new IllegalArgumentException("selected column should not be null or empty.");
        }

        if ("*".equals(columnName)) {
            if (aggregateFunction != null || as != null) {
                throw new IllegalArgumentException("aggregate function and alias can not be used when selecting all columns.");
            }
        }

        this.columnName = columnName;
        this.aggregateFunction = aggregateFunction;
        this.as = as;
    }

    private String sqlStr;

    public String getSqlStr(String tableName) {
        if (sqlStr == null) {
            StringBuilder stringBuilder = new StringBuilder();
            String col = this.columnName;
            if (tableName != null) {
                col = tableName + "." + columnName;
            }

            if (this.aggregateFunction != null) {
                stringBuilder.append(this.aggregateFunction.toString() + "( " + col + " ) ");
            } else {
                if (!"*".equals(columnName)) {
                    stringBuilder.append(col + " ");
                } else {
                    stringBuilder.append(columnName + " ");
                }
            }

            if (as != null) {
                stringBuilder.append(as.getSqlStr() + " ");
            }
            this.sqlStr = stringBuilder.toString();
        }
        return this.sqlStr;
    }
}