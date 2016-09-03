package com.tms.relational.clause.filter;

import lombok.NoArgsConstructor;

/**
 * Created by yzheng on 8/28/16.
 */

@NoArgsConstructor
public class FilterClauseException extends RuntimeException {

    public FilterClauseException(String message) { super(message); }

    public FilterClauseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterClauseException(Throwable cause) {
        super(cause);
    }

    public FilterClauseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
