package com.tms.relational.clause.filter;

import com.tms.relational.common.PreparedStatementFragment;
import java.util.List;
import lombok.Getter;

/**
 * Created by yzheng on 9/1/16.
 */

@Getter
public class BetweenFilterClause implements DBQueryFilter {
    private String column;
    private boolean not = true;
    List<Object> interval;

    public BetweenFilterClause(String column, List<Object> interval) {
        this(column, true, interval);

    }

    public BetweenFilterClause(String column, boolean not, List<Object> interval) {
        if (column == null || column.length() == 0) {
            throw new IllegalArgumentException("Column name should not be null or empty.");
        }

        if (interval == null || interval.size() != 2) {
            throw new IllegalArgumentException("The interval in between clause is not valid.");
        }

        this.column = column;
        this.not = not;
    }

    private PreparedStatementFragment preparedStatementFragment;
    @Override
    public PreparedStatementFragment getPreparedStatementFragment(String tableName) {
        if (preparedStatementFragment != null) {
            return preparedStatementFragment;
        }

        StringBuilder stringBuilder = new StringBuilder();
        String col = column;
        if (tableName != null) {
            col = tableName + "." + column;
        }

        stringBuilder.append(col);
        if (not) {  stringBuilder.append(" NOT"); }
        stringBuilder.append(" BETWEEN ? AND ? ");

        this.preparedStatementFragment = new PreparedStatementFragment(this.interval, stringBuilder.toString());
        return this.preparedStatementFragment;
    }
}
