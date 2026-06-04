package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.VideoMaterialDAO;
import com.example.digitalhuman.entity.VideoMaterial;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 视频素材接口
 * GET /api/videos - 获取视频列表
 * GET /api/videos?id=1 - 获取单个视频
 * GET /api/videos?category=科普 - 按分类获取
 * POST /api/videos - 新增视频素材
 * PUT /api/videos - 更新视频素材
 * DELETE /api/videos?id=1 - 删除视频素材
 */
@WebServlet("/api/videos")
public class VideoMaterialServlet extends BaseServlet {

    private final VideoMaterialDAO videoDAO = new VideoMaterialDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            String category = req.getParameter("category");
            String userId = req.getParameter("userId");

            if (id != null) {
                VideoMaterial video = videoDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), video);
            } else if (category != null) {
                List<VideoMaterial> list = videoDAO.findByCategory(category);
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else if (userId != null) {
                List<VideoMaterial> list = videoDAO.findByUserId(Long.parseLong(userId));
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else {
                List<VideoMaterial> list = videoDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取视频素材失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            VideoMaterial video = parseBody(req, VideoMaterial.class);
            if (video.getUserId() == null) {
                video.setUserId(1L);
            }
            Long id = videoDAO.save(video);
            video.setId(id);
            JsonUtil.writeSuccess(resp.getWriter(), video);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "新增视频素材失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            VideoMaterial video = parseBody(req, VideoMaterial.class);
            videoDAO.update(video);
            JsonUtil.writeSuccess(resp.getWriter(), video);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "更新视频素材失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            videoDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除视频素材失败: " + e.getMessage());
        }
    }
}
