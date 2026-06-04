package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.VideoMaterial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 视频素材 DAO
 */
public class VideoMaterialDAO extends BaseDAO {

    private VideoMaterial mapVideoMaterial(ResultSet rs) {
        try {
            VideoMaterial v = new VideoMaterial();
            v.setId(rs.getLong("id"));
            v.setTitle(rs.getString("title"));
            v.setDescription(rs.getString("description"));
            v.setVideoUrl(rs.getString("video_url"));
            v.setThumbnail(rs.getString("thumbnail"));
            v.setCategory(rs.getString("category"));
            v.setUserId(rs.getLong("user_id"));
            v.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            return v;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<VideoMaterial> findAll() throws SQLException {
        String sql = "SELECT * FROM dh_video_material ORDER BY id DESC";
        return query(sql, this::mapVideoMaterial);
    }

    public List<VideoMaterial> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM dh_video_material WHERE user_id = ? OR user_id IS NULL ORDER BY id DESC";
        return query(sql, this::mapVideoMaterial, userId);
    }

    public VideoMaterial findById(Long id) throws SQLException {
        String sql = "SELECT * FROM dh_video_material WHERE id = ?";
        return queryOne(sql, this::mapVideoMaterial, id);
    }

    public List<VideoMaterial> findByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM dh_video_material WHERE category = ? ORDER BY id DESC";
        return query(sql, this::mapVideoMaterial, category);
    }

    public Long save(VideoMaterial video) throws SQLException {
        String sql = "INSERT INTO dh_video_material (title, description, video_url, thumbnail, category, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, video.getTitle(), video.getDescription(),
                video.getVideoUrl(), video.getThumbnail(), video.getCategory(), video.getUserId());
    }

    public int update(VideoMaterial video) throws SQLException {
        String sql = "UPDATE dh_video_material SET title=?, description=?, video_url=?, thumbnail=?, category=? WHERE id=?";
        return update(sql, video.getTitle(), video.getDescription(),
                video.getVideoUrl(), video.getThumbnail(), video.getCategory(), video.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_video_material WHERE id = ?";
        return update(sql, id);
    }
}
