package com.tms.relational.clause.filter;

import com.tms.relational.common.PreparedStatementFragment;

/**
 * Created by yzheng on 8/28/16.
 */

public interface DBQueryFilter {

    PreparedStatementFragment getPreparedStatementFragment(String tableName);
}
