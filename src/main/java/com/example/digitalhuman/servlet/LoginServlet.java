package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.UserDAO;
import com.example.digitalhuman.entity.User;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口
 * POST /api/login
 */
@WebServlet("/api/login")
public class LoginServlet extends BaseServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Map<String, String> params = parseBody(req, Map.class);
            String username = params.get("username");
            String password = params.get("password");

            User user = userDAO.login(username, password);
            if (user != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("userId", user.getId());
                result.put("username", user.getUsername());
                result.put("nickname", user.getNickname());
                // 简化：不使用 session，直接返回用户信息
                JsonUtil.writeSuccess(resp.getWriter(), result);
            } else {
                JsonUtil.writeError(resp.getWriter(), 401, "用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "登录失败: " + e.getMessage());
        }
    }
}
