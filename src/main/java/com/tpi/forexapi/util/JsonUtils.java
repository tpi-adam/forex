package com.tpi.forexapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * json utils
 */
public class JsonUtils {

    private JsonUtils() {}

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 把物件轉成json string
     *
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String toJsonString(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    /**
     * 把json string轉成物件
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T toObject(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

}
