package com.tms.relational.common;

import java.util.List;
import lombok.Getter;

/**
 * Created by yzheng on 8/28/16.
 */

@Getter
public class PreparedStatementFragment {

    private final List<Object> valueList;
    private String sqlString;

    public PreparedStatementFragment(List<Object> valueList, String sqlString) {
        this.valueList = valueList;
        this.sqlString = sqlString;
    }
}
