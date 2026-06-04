package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.AvatarConfigDAO;
import com.example.digitalhuman.entity.AvatarConfig;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 数字人形象配置接口
 * GET /api/avatars - 获取用户形象列表
 * GET /api/avatars?id=1 - 获取单个形象
 * POST /api/avatars - 创建形象配置
 * PUT /api/avatars - 更新形象配置
 * DELETE /api/avatars?id=1 - 删除形象配置
 */
@WebServlet("/api/avatars")
public class AvatarConfigServlet extends BaseServlet {

    private final AvatarConfigDAO avatarDAO = new AvatarConfigDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            String userId = req.getParameter("userId");

            if (id != null) {
                AvatarConfig config = avatarDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), config);
            } else if (userId != null) {
                List<AvatarConfig> list = avatarDAO.findByUserId(Long.parseLong(userId));
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else {
                // 默认返回用户ID为1的配置
                List<AvatarConfig> list = avatarDAO.findByUserId(1L);
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取形象配置失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            AvatarConfig config = parseBody(req, AvatarConfig.class);
            if (config.getUserId() == null) {
                config.setUserId(1L);
            }
            Long id = avatarDAO.save(config);
            config.setId(id);
            JsonUtil.writeSuccess(resp.getWriter(), config);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "创建形象配置失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            AvatarConfig config = parseBody(req, AvatarConfig.class);
            avatarDAO.update(config);
            JsonUtil.writeSuccess(resp.getWriter(), config);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "更新形象配置失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            avatarDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除形象配置失败: " + e.getMessage());
        }
    }
}
