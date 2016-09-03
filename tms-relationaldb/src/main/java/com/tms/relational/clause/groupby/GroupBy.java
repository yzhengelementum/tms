package com.tms.relational.clause.groupby;

import com.tms.relational.common.PreparedStatementFragment;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yzheng on 9/1/16.
 */

@Setter
@Getter
public class GroupBy {
    private String column;

    public GroupBy(String column) { this.column = column; }

    private PreparedStatementFragment preparedStatementFragment;

    public PreparedStatementFragment getPreparedStatementFragment(String tableName) {
        if (this.getPreparedStatementFragment() == null) {
            String col = column;
            if (tableName != null && tableName.length() > 0) {
                col = tableName + "." + column;
            }

            this.preparedStatementFragment = new PreparedStatementFragment(Collections.EMPTY_LIST, "GROUP BY " + col);
        }

        return this.preparedStatementFragment;
    }
}