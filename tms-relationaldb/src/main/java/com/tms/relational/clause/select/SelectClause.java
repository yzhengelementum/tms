package com.tms.relational.clause.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yzheng on 9/1/16.
 */

public class SelectClause {
    private List<SelectionComponent> selectionComponentList;

    private SelectClause(List<SelectionComponent> selectionComponents) {
        List<SelectionComponent> l = new ArrayList<>();
        l.addAll(selectionComponents);
        this.selectionComponentList = Collections.unmodifiableList(l);
    }

    private String sqlStr;

    public String getSqlStr() {
        if (this.sqlStr == null && this.selectionComponentList != null) {
            int size = this.selectionComponentList.size();
            if (size > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("SELECT ");
                for (int i = 0; i < size; ++i) {
                    stringBuilder.append(this.selectionComponentList.get(i).getSqlStr());
                    if (i != size - 1) {
                        stringBuilder.append(", ");
                    }
                }

                this.sqlStr = stringBuilder.toString();
            }
        }

        return this.sqlStr;
    }

    public static class Builder {
        private List<SelectionComponent> selectionComponents = new ArrayList<>();

        public Builder addSelectionComponent(SelectionComponent selectionComponent) {
            this.selectionComponents.add(selectionComponent);
            return this;
        }

        public SelectClause build() {
            return new SelectClause(this.selectionComponents);
        }
    }
}