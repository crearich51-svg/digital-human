package com.example.digitalhuman.servlet;

import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 基础 Servlet，提供通用方法
 */
public abstract class BaseServlet extends HttpServlet {

    protected void setCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    protected void setJsonResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    protected String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    protected <T> T parseBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        String body = readBody(request);
        return JsonUtil.fromJson(body, clazz);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCors(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
