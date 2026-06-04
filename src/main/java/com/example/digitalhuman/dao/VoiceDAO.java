package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.Voice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 声音配置 DAO
 */
public class VoiceDAO extends BaseDAO {

    private Voice mapVoice(ResultSet rs) {
        try {
            Voice v = new Voice();
            v.setId(rs.getLong("id"));
            v.setName(rs.getString("name"));
            v.setVoiceType(rs.getString("voice_type"));
            v.setLanguage(rs.getString("language"));
            v.setSampleUrl(rs.getString("sample_url"));
            v.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            return v;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Voice> findAll() throws SQLException {
        String sql = "SELECT * FROM dh_voice ORDER BY id";
        return query(sql, this::mapVoice);
    }

    public Voice findById(Long id) throws SQLException {
        String sql = "SELECT * FROM dh_voice WHERE id = ?";
        return queryOne(sql, this::mapVoice, id);
    }

    public Long save(Voice voice) throws SQLException {
        String sql = "INSERT INTO dh_voice (name, voice_type, language, sample_url) VALUES (?, ?, ?, ?)";
        return insert(sql, voice.getName(), voice.getVoiceType(),
                voice.getLanguage(), voice.getSampleUrl());
    }

    public int update(Voice voice) throws SQLException {
        String sql = "UPDATE dh_voice SET name=?, voice_type=?, language=?, sample_url=? WHERE id=?";
        return update(sql, voice.getName(), voice.getVoiceType(),
                voice.getLanguage(), voice.getSampleUrl(), voice.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_voice WHERE id = ?";
        return update(sql, id);
    }
}
