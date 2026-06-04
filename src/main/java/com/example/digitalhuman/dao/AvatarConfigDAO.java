package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.AvatarConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数字人形象配置 DAO
 */
public class AvatarConfigDAO extends BaseDAO {

    private AvatarConfig mapAvatarConfig(ResultSet rs) {
        try {
            AvatarConfig a = new AvatarConfig();
            a.setId(rs.getLong("id"));
            a.setUserId(rs.getLong("user_id"));
            a.setName(rs.getString("name"));
            a.setGender(rs.getString("gender"));
            a.setAvatarStyle(rs.getString("avatar_style"));
            a.setSkinColor(rs.getString("skin_color"));
            a.setHairColor(rs.getString("hair_color"));
            a.setHairStyle(rs.getString("hair_style"));
            a.setEyeStyle(rs.getString("eye_style"));
            a.setMouthStyle(rs.getString("mouth_style"));
            a.setClothingColor(rs.getString("clothing_color"));
            a.setClothingStyle(rs.getString("clothing_style"));
            a.setBackgroundColor(rs.getString("background_color"));
            a.setAvatarUrl(rs.getString("avatar_url"));
            a.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            return a;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AvatarConfig> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM dh_avatar_config WHERE user_id = ? ORDER BY id DESC";
        return query(sql, this::mapAvatarConfig, userId);
    }

    public AvatarConfig findById(Long id) throws SQLException {
        String sql = "SELECT * FROM dh_avatar_config WHERE id = ?";
        return queryOne(sql, this::mapAvatarConfig, id);
    }

    public Long save(AvatarConfig config) throws SQLException {
        String sql = "INSERT INTO dh_avatar_config (" +
                "user_id, name, gender, avatar_style, skin_color, hair_color, " +
                "hair_style, eye_style, mouth_style, clothing_color, clothing_style, " +
                "background_color, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Long id = insert(sql,
                config.getUserId(), config.getName(), config.getGender(), config.getAvatarStyle(),
                config.getSkinColor(), config.getHairColor(), config.getHairStyle(),
                config.getEyeStyle(), config.getMouthStyle(), config.getClothingColor(),
                config.getClothingStyle(), config.getBackgroundColor(), config.getAvatarUrl());
        // 更新 avatar_url
        if (id != null) {
            config.setId(id);
            String avatarUrl = config.generateAvatarUrl();
            update("UPDATE dh_avatar_config SET avatar_url = ? WHERE id = ?", avatarUrl, id);
            config.setAvatarUrl(avatarUrl);
        }
        return id;
    }

    public int update(AvatarConfig config) throws SQLException {
        String sql = "UPDATE dh_avatar_config SET name=?, gender=?, avatar_style=?, " +
                "skin_color=?, hair_color=?, hair_style=?, eye_style=?, mouth_style=?, " +
                "clothing_color=?, clothing_style=?, background_color=?, avatar_url=? WHERE id=?";
        String avatarUrl = config.generateAvatarUrl();
        config.setAvatarUrl(avatarUrl);
        return update(sql,
                config.getName(), config.getGender(), config.getAvatarStyle(),
                config.getSkinColor(), config.getHairColor(), config.getHairStyle(),
                config.getEyeStyle(), config.getMouthStyle(), config.getClothingColor(),
                config.getClothingStyle(), config.getBackgroundColor(), avatarUrl, config.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_avatar_config WHERE id = ?";
        return update(sql, id);
    }
}
