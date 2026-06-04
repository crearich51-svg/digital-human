package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.Character;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数字人角色 DAO
 */
public class CharacterDAO extends BaseDAO {

    private Character mapCharacter(ResultSet rs) {
        try {
            Character c = new Character();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setAvatar(rs.getString("avatar"));
            c.setDescription(rs.getString("description"));
            c.setCategory(rs.getString("category"));
            c.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Character> findAll() throws SQLException {
        String sql = "SELECT * FROM dh_character ORDER BY id";
        return query(sql, this::mapCharacter);
    }

    public Character findById(Long id) throws SQLException {
        String sql = "SELECT * FROM dh_character WHERE id = ?";
        return queryOne(sql, this::mapCharacter, id);
    }

    public List<Character> findByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM dh_character WHERE category = ? ORDER BY id";
        return query(sql, this::mapCharacter, category);
    }

    public Long save(Character character) throws SQLException {
        String sql = "INSERT INTO dh_character (name, avatar, description, category) VALUES (?, ?, ?, ?)";
        return insert(sql, character.getName(), character.getAvatar(),
                character.getDescription(), character.getCategory());
    }

    public int update(Character character) throws SQLException {
        String sql = "UPDATE dh_character SET name=?, avatar=?, description=?, category=? WHERE id=?";
        return update(sql, character.getName(), character.getAvatar(),
                character.getDescription(), character.getCategory(), character.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM dh_character WHERE id = ?";
        return update(sql, id);
    }
}
