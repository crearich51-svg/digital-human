package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 生成任务 DAO
 */
public class TaskDAO extends BaseDAO {

    private Task mapTask(ResultSet rs) {
        try {
            Task t = new Task();
            t.setId(rs.getLong("id"));
            t.setScriptId(rs.getLong("script_id"));
            try { t.setAvatarConfigId(rs.getLong("avatar_config_id")); } catch (SQLException e) {}
            try { t.setVideoMaterialId(rs.getLong("video_material_id")); } catch (SQLException e) {}
            try { t.setVoiceType(rs.getString("voice_type")); } catch (SQLException e) {}
            t.setStatus(rs.getString("status"));
            t.setProgress(rs.getInt("progress"));
            t.setResultUrl(rs.getString("result_url"));
            t.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            t.setFinishTime(toLocalDateTime(rs.getTimestamp("finish_time")));
            try { t.setScriptTitle(rs.getString("script_title")); } catch (SQLException e) {}
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> findAll() throws SQLException {
        String sql = "SELECT t.*, s.title as script_title FROM dh_task t " +
                "LEFT JOIN dh_script s ON t.script_id = s.id ORDER BY t.id DESC";
        return query(sql, this::mapTask);
    }

    public Task findById(Long id) throws SQLException {
        String sql = "SELECT t.*, s.title as script_title FROM dh_task t " +
                "LEFT JOIN dh_script s ON t.script_id = s.id WHERE t.id = ?";
        return queryOne(sql, this::mapTask, id);
    }

    public Long save(Task task) throws SQLException {
        String sql = "INSERT INTO dh_task (script_id, avatar_config_id, video_material_id, voice_type, status, progress) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, task.getScriptId(), task.getAvatarConfigId(),
                task.getVideoMaterialId(), task.getVoiceType(),
                task.getStatus(), task.getProgress());
    }

    public int updateProgress(Long id, int progress, String status) throws SQLException {
        String sql = "UPDATE dh_task SET progress=?, status=? WHERE id=?";
        return update(sql, progress, status, id);
    }

    public int finish(Long id, String resultUrl) throws SQLException {
        String sql = "UPDATE dh_task SET progress=100, status='FINISHED', result_url=?, finish_time=CURRENT_TIMESTAMP WHERE id=?";
        return update(sql, resultUrl, id);
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_task WHERE id = ?";
        return update(sql, id);
    }
}
