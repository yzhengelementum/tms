package com.tms.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by yzheng on 8/23/16.
 */

public class ObjectMapperSingleton {
    private final static ObjectMapper INSTANCE = new ObjectMapper();
    public static ObjectMapper getInstance() { return INSTANCE; }
}
