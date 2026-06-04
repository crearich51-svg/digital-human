package com.example.digitalhuman.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON 工具类
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public static void writeJson(PrintWriter writer, int code, String message, Object data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", data);
        writer.write(toJson(result));
    }

    public static void writeSuccess(PrintWriter writer, Object data) throws IOException {
        writeJson(writer, 200, "success", data);
    }

    public static void writeError(PrintWriter writer, String message) throws IOException {
        writeJson(writer, 500, message, null);
    }

    public static void writeError(PrintWriter writer, int code, String message) throws IOException {
        writeJson(writer, code, message, null);
    }
}
