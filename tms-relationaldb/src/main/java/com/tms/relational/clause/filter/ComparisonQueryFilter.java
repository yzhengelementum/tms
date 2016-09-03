package com.tms.relational.clause.filter;

import com.tms.relational.common.PreparedStatementFragment;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by yzheng on 8/28/16.
 */

@Getter
public class ComparisonQueryFilter implements DBQueryFilter {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum CompareOp {
        EQ("$eq", "="),
        NEQ("$ne", "<>"),
        GT("$gt", ">"),
        GTE("$gte", ">="),
        LT("$le", "<"),
        LTE("$lte", "<=");

        private String jsonStr;
        private String sqlOp;

        public static CompareOp fromJsonStr(String jsonStr) {
            for (CompareOp op : values()) {
                if (op.jsonStr.equals(jsonStr)) {
                    return op;
                }
            }

            throw new IllegalArgumentException(jsonStr + " can not be converted to a CompareOp.");
        }
    }

    @NotNull
    private String columnName;
    @NotNull
    private CompareOp compareOp;
    private Object value;

    public ComparisonQueryFilter(String columnName, CompareOp compareOp, Object value) {
        if (!isSimpleLiteralValue(value)) {
            throw new FilterClauseException("Need a simple literal value to build a comparison filter.");
        }

        this.columnName = columnName;
        this.compareOp = compareOp;
        this.value = value;
    }

    private boolean isSimpleLiteralValue(Object o) {
        if (o == null)  return true;
        return o instanceof Boolean || o instanceof String || o instanceof Date || o instanceof Number;
    }

    private PreparedStatementFragment preparedStatementFragment;

    @Override
    public PreparedStatementFragment getPreparedStatementFragment(String tableName) {
        if (this.preparedStatementFragment != null) {    return this.preparedStatementFragment; }

        String _colName = this.columnName;
        if (tableName != null && _colName.indexOf(".") < 0) {
            _colName = tableName + "." + _colName;
        }

        PreparedStatementFragment psFragment;

        if (this.value == null) {
            if (CompareOp.EQ == this.compareOp) {
                psFragment = new PreparedStatementFragment(Collections.EMPTY_LIST, _colName + " is null ");
            } else if (CompareOp.NEQ == this.compareOp) {
                psFragment = new PreparedStatementFragment(Collections.EMPTY_LIST, _colName + " is not null ");
            } else {
                throw new FilterClauseException("null can be used equal and unequal operator only in ComparisonClause");
            }
        } else {
            psFragment = new PreparedStatementFragment(Arrays.asList(this.value), _colName + this.compareOp.getSqlOp() + " ? ");
        }

        this.preparedStatementFragment = psFragment;

        return preparedStatementFragment;
    }
}