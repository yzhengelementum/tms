package com.tms.relational.clause.filter;

import com.tms.relational.common.PreparedStatementFragment;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by yzheng on 8/28/16.
 */

@Getter
public class AndOrFilterClause implements DBQueryFilter {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    enum LogicOp {
        AND("$and"),
        OR("$or");

        private String jsonStr;

        public static LogicOp fromJsonStr(String jsonStr) {
            for (LogicOp op : values()) {
                if (op.getJsonStr().equals(jsonStr)) {
                    return op;
                }
            }

            throw new IllegalArgumentException(jsonStr);
        }
    }

    @NotNull
    private LogicOp logicOp;
    @NotNull
    private List<DBQueryFilter> filters;

    public AndOrFilterClause(LogicOp logicOp, List<DBQueryFilter> filters) {
        this.logicOp = logicOp;
        this.filters = filters;
    }

    private PreparedStatementFragment preparedStatementFragment;
    @Override
    public PreparedStatementFragment getPreparedStatementFragment(String tableName) {
        if (this.preparedStatementFragment == null) {

            PreparedStatementFragment psFragment;
            List<Object> valueList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("( ");
            int size = this.filters.size();

            for (int i = 0; i < size; ++i) {
                //TODO: how about the filters for different tables?
                PreparedStatementFragment ps = this.filters.get(i).getPreparedStatementFragment(tableName);
                sb.append(ps.getSqlString());
                if (i != size - 1) {
                    sb.append(this.logicOp + " ");
                }
                valueList.addAll(ps.getValueList());
            }

            sb.append(") ");
            psFragment = new PreparedStatementFragment(valueList, sb.toString());

            this.preparedStatementFragment = psFragment;
        }

        return this.preparedStatementFragment;
    }
}