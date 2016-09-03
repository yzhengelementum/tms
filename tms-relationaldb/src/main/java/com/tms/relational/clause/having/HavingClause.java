package com.tms.relational.clause.having;

import com.tms.relational.common.PreparedStatementFragment;
import com.tms.relational.clause.filter.DBQueryFilter;

/**
 * Created by yzheng on 9/1/16.
 */

public class HavingClause {

    private DBQueryFilter dbQueryFilter;

    public HavingClause(DBQueryFilter dbQueryFilter) {
        this.dbQueryFilter = dbQueryFilter;
    }

    private PreparedStatementFragment preparedStatementFragment;

    public PreparedStatementFragment getPreparedStatementFragment(String tableName) {
        if (this.preparedStatementFragment == null) {
            if (dbQueryFilter != null) {
                this.preparedStatementFragment = new PreparedStatementFragment(
                        dbQueryFilter.getPreparedStatementFragment(tableName).getValueList(),
                "HAVING " + dbQueryFilter.getPreparedStatementFragment(tableName).getSqlString()
                );
            }
        }

        return this.preparedStatementFragment;
    }
}