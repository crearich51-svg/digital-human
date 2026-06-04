package com.example.digitalhuman.servlet;

import com.example.digitalhuman.dao.CharacterDAO;
import com.example.digitalhuman.entity.Character;
import com.example.digitalhuman.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 数字人角色接口
 * GET /api/characters - 获取角色列表
 * GET /api/characters?id=1 - 获取单个角色
 * POST /api/characters - 新增角色
 * PUT /api/characters - 更新角色
 * DELETE /api/characters?id=1 - 删除角色
 */
@WebServlet("/api/characters")
public class CharacterServlet extends BaseServlet {

    private final CharacterDAO characterDAO = new CharacterDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            String category = req.getParameter("category");

            if (id != null) {
                Character character = characterDAO.findById(Long.parseLong(id));
                JsonUtil.writeSuccess(resp.getWriter(), character);
            } else if (category != null) {
                List<Character> list = characterDAO.findByCategory(category);
                JsonUtil.writeSuccess(resp.getWriter(), list);
            } else {
                List<Character> list = characterDAO.findAll();
                JsonUtil.writeSuccess(resp.getWriter(), list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "获取角色失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Character character = parseBody(req, Character.class);
            Long id = characterDAO.save(character);
            character.setId(id);
            JsonUtil.writeSuccess(resp.getWriter(), character);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "新增角色失败: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            Character character = parseBody(req, Character.class);
            characterDAO.update(character);
            JsonUtil.writeSuccess(resp.getWriter(), character);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "更新角色失败: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        setJsonResponse(resp);

        try {
            String id = req.getParameter("id");
            characterDAO.delete(Long.parseLong(id));
            JsonUtil.writeSuccess(resp.getWriter(), null);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.writeError(resp.getWriter(), "删除角色失败: " + e.getMessage());
        }
    }
}
