package com.gotta_watch_them_all.app.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    public static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(String json, Class<T> concernedClass) {
        try {
            return new ObjectMapper().readValue(json, concernedClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
