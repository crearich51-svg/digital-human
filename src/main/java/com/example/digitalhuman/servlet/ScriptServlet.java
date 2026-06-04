package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.ScriptDAO;
import com.example.digitalhuman.entity.Script;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 脚本接口
 * GET /api/scripts - 获取脚本列表
 * GET /api/scripts?id=1 - 获取单个脚本
 * GET /api/scripts?userId=1 - 获取用户的脚本列表
 * POST /api/scripts - 新增脚本
 * PUT /api/scripts - 更新脚本
 * DELETE /api/scripts?id=1 - 删除脚本
 */
@WebServlet("/api/scripts")
public class ScriptServlet extends BaseServlet {

    private final ScriptDAO scriptDAO = new ScriptDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            String userId = req.getParameter("userId");

            if (id != null) {
                Script script = scriptDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), script);
            } else if (userId != null) {
                List<Script> list = scriptDAO.findByUserId(Long.parseLong(userId));
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else {
                List<Script> list = scriptDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取脚本失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Script script = parseBody(req, Script.class);
            if (script.getUserId() == null) {
                script.setUserId(1L); // 默认用户
            }
            Long id = scriptDAO.save(script);
            script.setId(id);
            JsonUtil.writeSuccess(resp.getWriter(), script);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "新增脚本失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Script script = parseBody(req, Script.class);
            scriptDAO.update(script);
            JsonUtil.writeSuccess(resp.getWriter(), script);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "更新脚本失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            scriptDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除脚本失败: " + e.getMessage());
        }
    }
}
