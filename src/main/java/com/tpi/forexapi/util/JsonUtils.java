package com.tpi.forexapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtils {

    private JsonUtils() {}

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static String toJsonString(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toObject(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

}
