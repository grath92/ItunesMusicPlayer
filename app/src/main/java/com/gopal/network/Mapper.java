package com.gopal.network;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Mapper {

    private static ObjectMapper sMapper;

    /**
     * Get the ObjectMapper instance, creates new if needed.
     *
     * @return Singleton ObjectMapper instance.
     */
    public static ObjectMapper getInstance() {
        if (sMapper == null) {
            // Making it thread safe
            synchronized (ObjectMapper.class) {
                if (sMapper == null) {
                    sMapper = new ObjectMapper();
                    sMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }
        return sMapper;
    }
}
