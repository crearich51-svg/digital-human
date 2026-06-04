package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.AvatarConfigDAO;
import com.example.digitalhuman.dao.TaskDAO;
import com.example.digitalhuman.dao.VideoMaterialDAO;
import com.example.digitalhuman.dao.WorkDAO;
import com.example.digitalhuman.entity.AvatarConfig;
import com.example.digitalhuman.entity.Task;
import com.example.digitalhuman.entity.VideoMaterial;
import com.example.digitalhuman.entity.Work;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生成任务接口
 * GET /api/tasks - 获取任务列表
 * GET /api/tasks?id=1 - 获取单个任务
 * POST /api/tasks - 创建生成任务（模拟生成数字人视频）
 * DELETE /api/tasks?id=1 - 删除任务
 */
@WebServlet("/api/tasks")
public class TaskServlet extends BaseServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final WorkDAO workDAO = new WorkDAO();
    private final AvatarConfigDAO avatarDAO = new AvatarConfigDAO();
    private final VideoMaterialDAO videoDAO = new VideoMaterialDAO();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            if (id != null) {
                Task task = taskDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), task);
            } else {
                List<Task> list = taskDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取任务失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Task task = parseBody(req, Task.class);
            task.setStatus("PENDING");
            task.setProgress(0);
            Long taskId = taskDAO.save(task);
            task.setId(taskId);

            // 异步模拟视频生成过程
            executor.submit(() -> simulateGeneration(task));

            JsonUtil.writeSuccess(resp.getWriter(), task);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "创建任务失败: " + e.getMessage());
        }
    }

    /**
     * 模拟数字人视频生成过程
     * 包含：形象渲染、语音合成、视频合成等步骤
     */
    private void simulateGeneration(Task task) {
        Long taskId = task.getId();
        Long scriptId = task.getScriptId();
        Long avatarConfigId = task.getAvatarConfigId();
        Long videoMaterialId = task.getVideoMaterialId();
        String voiceType = task.getVoiceType();

        System.out.println("===== 任务调试信息 =====");
        System.out.println("taskId: " + taskId);
        System.out.println("avatarConfigId: " + avatarConfigId);
        System.out.println("videoMaterialId: " + videoMaterialId);
        System.out.println("voiceType: " + voiceType);
        System.out.println("scriptTitle: " + task.getScriptTitle());
        System.out.println("scriptContent: " + (task.getScriptContent() != null ? task.getScriptContent().substring(0, Math.min(50, task.getScriptContent().length())) + "..." : "null"));
        System.out.println("========================");

        try {
            // 步骤1: 加载数字人形象配置
            AvatarConfig avatar = avatarConfigId != null && avatarConfigId > 0 ? avatarDAO.findById(avatarConfigId) : null;
            // 步骤2: 加载视频素材
            VideoMaterial video = videoMaterialId != null && videoMaterialId > 0 ? videoDAO.findById(videoMaterialId) : null;

            System.out.println("avatar: " + (avatar != null ? avatar.getName() : "null"));
            System.out.println("video: " + (video != null ? video.getTitle() : "null"));

            // 模拟生成进度
            String[] steps = {"正在加载数字人形象...", "正在渲染形象...", "正在合成语音...", "正在合成视频...", "正在生成最终作品..."};
            for (int i = 0; i < steps.length; i++) {
                Thread.sleep(1200);
                int progress = (i + 1) * 20;
                taskDAO.updateProgress(taskId, progress, "PROCESSING");
            }

            Thread.sleep(500);

            // 生成完成，创建作品
            String videoUrl = video != null ? video.getVideoUrl() : "https://www.w3schools.com/html/mov_bbb.mp4";
            String thumbnail = video != null ? video.getThumbnail() :
                    "https://api.dicebear.com/7.x/avataaars/svg?seed=" + System.currentTimeMillis();
            String avatarUrl = avatar != null ? avatar.getAvatarUrl() : null;

            taskDAO.finish(taskId, videoUrl);

            // 创建作品记录
            Work work = new Work();
            work.setTitle(task.getScriptTitle() != null && !task.getScriptTitle().isEmpty()
                    ? task.getScriptTitle()
                    : "数字人作品_" + System.currentTimeMillis());
            work.setVideoUrl(videoUrl);
            work.setThumbnail(thumbnail);
            work.setAvatarUrl(avatarUrl);
            work.setScriptContent(task.getScriptContent());
            work.setScriptId(scriptId);
            work.setAvatarConfigId(avatarConfigId);
            work.setVideoMaterialId(videoMaterialId);
            work.setVoiceType(voiceType);
            work.setUserId(1L);
            workDAO.save(work);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                taskDAO.updateProgress(taskId, 0, "FAILED");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            taskDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除任务失败: " + e.getMessage());
        }
    }
}
