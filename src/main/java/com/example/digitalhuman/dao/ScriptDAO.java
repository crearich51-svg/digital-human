package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.Script;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 脚本 DAO
 */
public class ScriptDAO extends BaseDAO {

    private Script mapScript(ResultSet rs) {
        try {
            Script s = new Script();
            s.setId(rs.getLong("id"));
            s.setTitle(rs.getString("title"));
            s.setContent(rs.getString("content"));
            s.setCharacterId(rs.getLong("character_id"));
            s.setVoiceId(rs.getLong("voice_id"));
            s.setUserId(rs.getLong("user_id"));
            s.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            try { s.setCharacterName(rs.getString("character_name")); } catch (SQLException e) {}
            try { s.setVoiceName(rs.getString("voice_name")); } catch (SQLException e) {}
            return s;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Script> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT s.*, c.name as character_name, v.name as voice_name " +
                "FROM dh_script s LEFT JOIN dh_character c ON s.character_id = c.id " +
                "LEFT JOIN dh_voice v ON s.voice_id = v.id " +
                "WHERE s.user_id = ? ORDER BY s.id DESC";
        return query(sql, this::mapScript, userId);
    }

    public Script findById(Long id) throws SQLException {
        String sql = "SELECT s.*, c.name as character_name, v.name as voice_name " +
                "FROM dh_script s LEFT JOIN dh_character c ON s.character_id = c.id " +
                "LEFT JOIN dh_voice v ON s.voice_id = v.id " +
                "WHERE s.id = ?";
        return queryOne(sql, this::mapScript, id);
    }

    public List<Script> findAll() throws SQLException {
        String sql = "SELECT s.*, c.name as character_name, v.name as voice_name " +
                "FROM dh_script s LEFT JOIN dh_character c ON s.character_id = c.id " +
                "LEFT JOIN dh_voice v ON s.voice_id = v.id " +
                "ORDER BY s.id DESC";
        return query(sql, this::mapScript);
    }

    public Long save(Script script) throws SQLException {
        String sql = "INSERT INTO dh_script (title, content, character_id, voice_id, user_id) VALUES (?, ?, ?, ?, ?)";
        return insert(sql, script.getTitle(), script.getContent(),
                script.getCharacterId(), script.getVoiceId(), script.getUserId());
    }

    public int update(Script script) throws SQLException {
        String sql = "UPDATE dh_script SET title=?, content=?, character_id=?, voice_id=? WHERE id=?";
        return update(sql, script.getTitle(), script.getContent(),
                script.getCharacterId(), script.getVoiceId(), script.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_script WHERE id = ?";
        return update(sql, id);
    }
}
