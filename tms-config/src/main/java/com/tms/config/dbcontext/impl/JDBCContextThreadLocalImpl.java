package com.tms.config.dbcontext.impl;

import com.tms.config.dbcontext.JDBCContext;

/**
 * Created by yzheng on 8/28/16.
 */

public class JDBCContextThreadLocalImpl {

    private final ThreadLocal<JDBCContext> HOLDER = new ThreadLocal<>();
    public static JDBCContextThreadLocalImpl INSTANCE = new JDBCContextThreadLocalImpl();

    private JDBCContextThreadLocalImpl() {}

    public void set(JDBCContext jdbcContext) {  HOLDER.set(jdbcContext); }

    public void clear() {  HOLDER.remove(); }

    public JDBCContext get() {  return HOLDER.get(); }
}
