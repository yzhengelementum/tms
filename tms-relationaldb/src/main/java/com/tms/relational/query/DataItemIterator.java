package com.tms.relational.query;

import java.util.Iterator;

/**
 * Created by yzheng on 8/31/16.
 */

//use to reader data from database

public class DataItemIterator implements Iterator<Object>, AutoCloseable {
    @Override
    public void close() throws Exception {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public void remove() {

    }
}
