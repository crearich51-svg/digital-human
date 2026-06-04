package com.example.digitalhuman.dao;

import com.example.digitalhuman.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户 DAO
 */
public class UserDAO extends BaseDAO {

    private User mapUser(ResultSet rs) {
        try {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            user.setCreateTime(toLocalDateTime(rs.getTimestamp("create_time")));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM sys_user WHERE username = ? AND password = ?";
        return queryOne(sql, this::mapUser, username, password);
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT * FROM sys_user WHERE id = ?";
        return queryOne(sql, this::mapUser, id);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM sys_user ORDER BY id";
        return query(sql, this::mapUser);
    }

    public Long save(User user) throws SQLException {
        String sql = "INSERT INTO sys_user (username, password, nickname) VALUES (?, ?, ?)";
        return insert(sql, user.getUsername(), user.getPassword(), user.getNickname());
    }

    public int update(User user) throws SQLException {
        String sql = "UPDATE sys_user SET nickname = ? WHERE id = ?";
        return update(sql, user.getNickname(), user.getId());
    }

    public int delete(Long id) throws SQLException {
        String sql = "DELETE FROM sys_user WHERE id = ?";
        return update(sql, id);
    }
}
