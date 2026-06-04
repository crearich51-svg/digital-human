package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.WorkDAO;
import com.example.digitalhuman.entity.Work;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 作品接口
 * GET /api/works - 获取作品列表
 * GET /api/works?id=1 - 获取单个作品
 * GET /api/works?userId=1 - 获取用户的作品列表
 * POST /api/works/view?id=1 - 增加播放量
 * DELETE /api/works?id=1 - 删除作品
 */
@WebServlet("/api/works")
public class WorkServlet extends BaseServlet {

    private final WorkDAO workDAO = new WorkDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            String userId = req.getParameter("userId");

            if (id != null) {
                Work work = workDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), work);
            } else if (userId != null) {
                List<Work> list = workDAO.findByUserId(Long.parseLong(userId));
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else {
                List<Work> list = workDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取作品失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String path = req.getPathInfo();
            if ("/view".equals(path)) {
                String id = req.getParameter("id");
                workDAO.incrementViews(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), null);
            } else {
                // 新增作品
                Work work = parseBody(req, Work.class);
                if (work.getUserId() == null) {
                    work.setUserId(1L);
                }
                Long id = workDAO.save(work);
                work.setId(id);
                JsonUtil.writeSuccess(resp.getWriter(), work);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "操作失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            workDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除作品失败: " + e.getMessage());
        }
    }
}
