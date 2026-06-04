package com.example.digitalhuman.dao;

import com.example.digitalhuman.config.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 基础 DAO 类，提供通用的数据库操作
 */
public class BaseDAO {

    protected <T> List<T> query(String sql, Function<ResultSet, T> mapper, Object... params) throws SQLException {
        List<T> list = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParams(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.apply(rs));
                }
            }
        }
        return list;
    }

    protected <T> T queryOne(String sql, Function<ResultSet, T> mapper, Object... params) throws SQLException {
        List<T> list = query(sql, mapper, params);
        return list.isEmpty() ? null : list.get(0);
    }

    protected int update(String sql, Object... params) throws SQLException {
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParams(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    protected Long insert(String sql, Object... params) throws SQLException {
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(pstmt, params);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    private void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    protected Timestamp toTimestamp(java.time.LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }

    protected java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
