package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.VoiceDAO;
import com.example.digitalhuman.entity.Voice;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 声音配置接口
 * GET /api/voices - 获取声音列表
 * GET /api/voices?id=1 - 获取单个声音
 * POST /api/voices - 新增声音
 * PUT /api/voices - 更新声音
 * DELETE /api/voices?id=1 - 删除声音
 */
@WebServlet("/api/voices")
public class VoiceServlet extends BaseServlet {

    private final VoiceDAO voiceDAO = new VoiceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            if (id != null) {
                Voice voice = voiceDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), voice);
            } else {
                List<Voice> list = voiceDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取声音配置失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Voice voice = parseBody(req, Voice.class);
            Long id = voiceDAO.save(voice);
            voice.setId(id);
            JsonUtil.writeSuccess(resp.getWriter(), voice);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "新增声音失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Voice voice = parseBody(req, Voice.class);
            voiceDAO.update(voice);
            JsonUtil.writeSuccess(resp.getWriter(), voice);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "更新声音失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            voiceDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除声音失败: " + e.getMessage());
        }
    }
}
