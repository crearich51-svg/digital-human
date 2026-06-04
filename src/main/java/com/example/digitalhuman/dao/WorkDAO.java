package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.Work;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 作品 DAO
 */
public class WorkDAO extends BaseDAO {

    private Work mapWork(ResultSet rs) {
        try {
            Work w = new Work();
            w.setId(rs.getLong("id"));
            w.setTitle(rs.getString("title"));
            w.setVideoUrl(rs.getString("video_url"));
            w.setThumbnail(rs.getString("thumbnail"));
            w.setScriptId(rs.getLong("script_id"));
            try { w.setAvatarConfigId(rs.getLong("avatar_config_id")); } catch (SQLException e) {}
            try { w.setVideoMaterialId(rs.getLong("video_material_id")); } catch (SQLException e) {}
            try { w.setVoiceType(rs.getString("voice_type")); } catch (SQLException e) {}
            w.setUserId(rs.getLong("user_id"));
            w.setViews(rs.getInt("views"));
            w.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            // 优先使用 dh_work 表自己的 script_content，如果为空再使用 dh_script 表的
            try {
                String workScriptContent = rs.getString("script_content");
                String joinedScriptContent = null;
                try { joinedScriptContent = rs.getString("joined_script_content"); } catch (SQLException e) {}
                w.setScriptContent(workScriptContent != null ? workScriptContent : joinedScriptContent);
            } catch (SQLException e) {}
            // 优先使用 dh_work 表自己的 avatar_url，如果为空再使用 dh_avatar_config 表的
            try {
                String workAvatarUrl = rs.getString("avatar_url");
                String joinedAvatarUrl = null;
                try { joinedAvatarUrl = rs.getString("joined_avatar_url"); } catch (SQLException e) {}
                w.setAvatarUrl(workAvatarUrl != null ? workAvatarUrl : joinedAvatarUrl);
            } catch (SQLException e) {}
            return w;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Work> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT w.*, s.content as joined_script_content, a.avatar_url as joined_avatar_url FROM dh_work w " +
                "LEFT JOIN dh_script s ON w.script_id = s.id " +
                "LEFT JOIN dh_avatar_config a ON w.avatar_config_id = a.id " +
                "WHERE w.user_id = ? ORDER BY w.id DESC";
        return query(sql, this::mapWork, userId);
    }

    public List<Work> findAll() throws SQLException {
        String sql = "SELECT w.*, s.content as joined_script_content, a.avatar_url as joined_avatar_url FROM dh_work w " +
                "LEFT JOIN dh_script s ON w.script_id = s.id " +
                "LEFT JOIN dh_avatar_config a ON w.avatar_config_id = a.id " +
                "ORDER BY w.id DESC";
        return query(sql, this::mapWork);
    }

    public Work findById(Long id) throws SQLException {
        String sql = "SELECT w.*, s.content as joined_script_content, a.avatar_url as joined_avatar_url FROM dh_work w " +
                "LEFT JOIN dh_script s ON w.script_id = s.id " +
                "LEFT JOIN dh_avatar_config a ON w.avatar_config_id = a.id " +
                "WHERE w.id = ?";
        return queryOne(sql, this::mapWork, id);
    }

    public Long save(Work work) throws SQLException {
        String sql = "INSERT INTO dh_work (title, video_url, thumbnail, script_id, avatar_config_id, video_material_id, voice_type, user_id, script_content, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, work.getTitle(), work.getVideoUrl(),
                work.getThumbnail(), work.getScriptId(),
                work.getAvatarConfigId(), work.getVideoMaterialId(),
                work.getVoiceType(), work.getUserId(),
                work.getScriptContent(), work.getAvatarUrl());
    }

    public int incrementViews(Long id) throws SQLException {
        String sql = "UPDATE dh_work SET views = views + 1 WHERE id = ?";
        return update(sql, id);
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_work WHERE id = ?";
        return update(sql, id);
    }
}
