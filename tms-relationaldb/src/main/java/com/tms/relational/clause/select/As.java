package com.tms.relational.clause.select;

/**
 * Created by yzheng on 9/1/16.
 */

import javax.validation.constraints.NotNull;

/**
 * alias
 */

public class As {
    @NotNull
    private String newColumnName;

    public As(String newColumnName) {
        this.newColumnName = newColumnName;
    }

    public String getSqlStr() {
        return "AS " + newColumnName + " ";
    }
}
