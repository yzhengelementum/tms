package com.tms.relational.clause.order;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yzheng on 8/28/16.
 */

@Getter
@Setter
public class OrderClause {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    enum Order {
        ASC("ASC"),
        DESC("DESC");

        private String sqlStr;
    }

    @Getter
    public static class OrderClauseComponent {
        private String columnName;
        private Order order;

        public OrderClauseComponent(String columnName, Order order) {
            if (order == null) {
                throw new IllegalArgumentException("order == null");
            }

            if (columnName == null) {
                throw new IllegalArgumentException("columnName == null");
            }

            this.columnName = columnName;
            this.order = order;
        }
    }

    private List<OrderClauseComponent> orderList;

    private OrderClause(List<OrderClauseComponent> orderList) {
        this.orderList = orderList;
    }

    public String getSqlStr(String tableName) {
        if (this.orderList == null || this.orderList.size() == 0) {
            return "";
        }

        int size = this.orderList.size();
        StringBuilder orderClauseSqlBuilder = new StringBuilder();
        orderClauseSqlBuilder.append(" ORDER BY ");

        for (int i = 0; i < size; ++i) {
            String colName = tableName + "." + this.orderList.get(i).getColumnName();
            orderClauseSqlBuilder.append(colName + " " + this.orderList.get(i).getOrder());

            if (i == (size - 1)) {
                orderClauseSqlBuilder.append(" ");
            } else {
                orderClauseSqlBuilder.append(", ");
            }
        }

        return orderClauseSqlBuilder.toString();
    }

    public static class OrderClauseBuilder {
        private List<OrderClauseComponent> orderClauseComponentList = new ArrayList<>();

        public OrderClauseBuilder addAscColumn(String columnName) {
            this.orderClauseComponentList.add(new OrderClauseComponent(columnName, Order.ASC));
            return this;
        }

        public OrderClauseBuilder addDescColumn(String columnName) {
            this.orderClauseComponentList.add(new OrderClauseComponent(columnName, Order.DESC));
            return this;
        }

        public OrderClause build() {
            return new OrderClause(this.orderClauseComponentList);
        }
    }
}