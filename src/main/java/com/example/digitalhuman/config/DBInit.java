package com.example.digitalhuman.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 数据库初始化监听器
 * 应用启动时自动创建表和初始化数据
 */
@WebListener
public class DBInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // 创建用户表
            stmt.execute("CREATE TABLE IF NOT EXISTS sys_user (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "nickname VARCHAR(100), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建数字人角色表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_character (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "avatar VARCHAR(500), " +
                    "description VARCHAR(500), " +
                    "category VARCHAR(50), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建声音配置表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_voice (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "voice_type VARCHAR(50), " +
                    "language VARCHAR(50), " +
                    "sample_url VARCHAR(500), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建脚本表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_script (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "content TEXT, " +
                    "character_id BIGINT, " +
                    "voice_id BIGINT, " +
                    "user_id BIGINT, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建生成任务表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_task (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "script_id BIGINT, " +
                    "status VARCHAR(20) DEFAULT 'PENDING', " +
                    "progress INT DEFAULT 0, " +
                    "result_url VARCHAR(500), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "finish_time TIMESTAMP)");

            // 创建作品表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_work (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "title VARCHAR(200) NOT NULL, " +                    "video_url VARCHAR(500), " +
                    "thumbnail TEXT, " +
                    "script_id BIGINT, " +
                    "avatar_config_id BIGINT, " +
                    "video_material_id BIGINT, " +
                    "voice_type VARCHAR(20), " +
                    "user_id BIGINT, " +
                    "views INT DEFAULT 0, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建数字人形象配置表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_avatar_config (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id BIGINT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "gender VARCHAR(10), " +
                    "avatar_style VARCHAR(50), " +
                    "skin_color VARCHAR(50), " +
                    "hair_color VARCHAR(50), " +
                    "hair_style VARCHAR(50), " +
                    "eye_style VARCHAR(50), " +
                    "mouth_style VARCHAR(50), " +
                    "clothing_color VARCHAR(50), " +
                    "clothing_style VARCHAR(50), " +
                    "background_color VARCHAR(50), " +
                    "avatar_url VARCHAR(500), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 创建视频素材表
            stmt.execute("CREATE TABLE IF NOT EXISTS dh_video_material (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "description TEXT, " +
                    "video_url VARCHAR(500) NOT NULL, " +
                    "thumbnail TEXT, " +
                    "category VARCHAR(50), " +
                    "user_id BIGINT, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 更新任务表，添加形象和视频关联
            stmt.execute("ALTER TABLE dh_task ADD COLUMN IF NOT EXISTS avatar_config_id BIGINT");
            stmt.execute("ALTER TABLE dh_task ADD COLUMN IF NOT EXISTS video_material_id BIGINT");
            stmt.execute("ALTER TABLE dh_task ADD COLUMN IF NOT EXISTS voice_type VARCHAR(20)");
            stmt.execute("ALTER TABLE dh_task ADD COLUMN IF NOT EXISTS script_title VARCHAR(200)");
            stmt.execute("ALTER TABLE dh_task ADD COLUMN IF NOT EXISTS script_content TEXT");

            // 更新作品表，添加文案和数字人字段
            stmt.execute("ALTER TABLE dh_work ADD COLUMN IF NOT EXISTS script_content TEXT");
            stmt.execute("ALTER TABLE dh_work ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(500)");

            // 初始化测试数据
            initTestData(stmt);

            System.out.println("===== 数据库初始化完成 =====");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库初始化失败", e);
        }
    }

    private void initTestData(Statement stmt) throws Exception {
        // 测试账号
        stmt.execute("INSERT INTO sys_user (username, password, nickname) VALUES " +
                "('admin', '123456', '管理员'), " +
                "('test', '123456', '测试用户')");

        // 预设数字人角色
        stmt.execute("INSERT INTO dh_character (name, avatar, description, category) VALUES " +
                "('知性女教师', 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher', '温柔知性的女性教师形象，适合教育讲解场景', '教育'), " +
                "('专业男主播', 'https://api.dicebear.com/7.x/avataaars/svg?seed=host', '专业稳重的男性主播形象，适合新闻播报场景', '传媒'), " +
                "('可爱卡通少女', 'https://api.dicebear.com/7.x/avataaars/svg?seed=cute', '活泼可爱的卡通少女形象，适合娱乐科普场景', '娱乐'), " +
                "('商务精英', 'https://api.dicebear.com/7.x/avataaars/svg?seed=business', '干练专业的商务人士形象，适合企业宣传场景', '商务'), " +
                "('历史讲解员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=history', '儒雅博学的历史讲解员形象，适合博物馆导览场景', '文化')");

        // 预设声音配置
        stmt.execute("INSERT INTO dh_voice (name, voice_type, language, sample_url) VALUES " +
                "('温柔女声', 'female', '中文', ''), " +
                "('沉稳男声', 'male', '中文', ''), " +
                "('活泼童声', 'child', '中文', ''), " +
                "('标准英语', 'female', '英文', ''), " +
                "('粤语女声', 'female', '粤语', '')");

        // 示例脚本
        stmt.execute("INSERT INTO dh_script (title, content, character_id, voice_id, user_id) VALUES " +
                "('太阳系介绍', '大家好，今天我来为大家介绍我们的太阳系。太阳系是以太阳为中心，和所有受到太阳引力约束的天体的集合体。包括八大行星，由离太阳从近到远的顺序为：水星、金星、地球、火星、木星、土星、天王星、海王星。', 1, 1, 1), " +
                "('公司产品发布会', '各位来宾，大家好！欢迎参加我们的新产品发布会。今天我将为大家介绍我们公司最新推出的智能产品系列...', 4, 2, 1)");

        // 示例作品
        stmt.execute("INSERT INTO dh_work (title, video_url, thumbnail, script_id, user_id, views, voice_type, script_content, avatar_url) VALUES " +
                "('太阳系科普讲解', 'https://www.w3schools.com/html/mov_bbb.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g1\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%231e3a8a;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23312e81;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g1)\" width=\"400\" height=\"225\"/><circle cx=\"200\" cy=\"112\" r=\"30\" fill=\"%23fbbf24\"/><circle cx=\"280\" cy=\"80\" r=\"8\" fill=\"%2394a3b8\"/><circle cx=\"320\" cy=\"130\" r=\"12\" fill=\"%23ef4444\"/><circle cx=\"120\" cy=\"150\" r=\"10\" fill=\"%233b82f6\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23e2e8f0\" font-size=\"16\" font-weight=\"bold\">太阳系科普</text></svg>', 1, 1, 128, 'female', '大家好，今天我来为大家介绍我们的太阳系。太阳系是以太阳为中心，和所有受到太阳引力约束的天体的集合体。包括八大行星，由离太阳从近到远的顺序为：水星、金星、地球、火星、木星、土星、天王星、海王星。', 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher&skinColor=ffdbac&hairColor=4a312c&backgroundColor=e0e7ff&clothingColor=1e40af'), " +
                "('产品介绍', 'https://www.w3schools.com/html/movie.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g2\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%23065f46;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23064e3b;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g2)\" width=\"400\" height=\"225\"/><rect x=\"100\" y=\"60\" width=\"200\" height=\"100\" rx=\"8\" fill=\"%23059669\"/><rect x=\"120\" y=\"80\" width=\"160\" height=\"8\" rx=\"4\" fill=\"%2310b981\"/><rect x=\"120\" y=\"95\" width=\"120\" height=\"6\" rx=\"3\" fill=\"%2334d399\"/><rect x=\"120\" y=\"110\" width=\"140\" height=\"6\" rx=\"3\" fill=\"%236ee7b7\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23d1fae5\" font-size=\"16\" font-weight=\"bold\">产品介绍</text></svg>', 2, 1, 256, 'male', '各位来宾，大家好！欢迎参加我们的新产品发布会。今天我将为大家介绍我们公司最新推出的智能产品系列...', 'https://api.dicebear.com/7.x/avataaars/svg?seed=host&skinColor=ffdbac&hairColor=2c1b18&backgroundColor=dcfce7&clothingColor=166534')");

        // 预设视频素材（科普视频）
        stmt.execute("INSERT INTO dh_video_material (title, description, video_url, thumbnail, category, user_id) VALUES " +
                "('太阳系科普', '介绍太阳系八大行星的科普视频', 'https://www.w3schools.com/html/mov_bbb.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g1\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%231e3a8a;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23312e81;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g1)\" width=\"400\" height=\"225\"/><circle cx=\"200\" cy=\"112\" r=\"30\" fill=\"%23fbbf24\"/><circle cx=\"280\" cy=\"80\" r=\"8\" fill=\"%2394a3b8\"/><circle cx=\"320\" cy=\"130\" r=\"12\" fill=\"%23ef4444\"/><circle cx=\"120\" cy=\"150\" r=\"10\" fill=\"%233b82f6\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23e2e8f0\" font-size=\"16\" font-weight=\"bold\">太阳系科普</text></svg>', '科普', NULL), " +
                "('自然风景', '美丽的自然风光展示视频', 'https://www.w3schools.com/html/movie.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g3\" x1=\"0%\" y1=\"0%\" x2=\"0%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%230ea5e9;stop-opacity:1\" /><stop offset=\"60%\" style=\"stop-color:%2322c55e;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23166534;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g3)\" width=\"400\" height=\"225\"/><circle cx=\"320\" cy=\"50\" r=\"20\" fill=\"%23fde047\"/><polygon points=\"0,180 80,100 160,180\" fill=\"%23166534\"/><polygon points=\"120,180 200,80 280,180\" fill=\"%2315803d\"/><polygon points=\"240,180 320,110 400,180\" fill=\"%23166534\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23dcfce7\" font-size=\"16\" font-weight=\"bold\">自然风景</text></svg>', '自然', NULL), " +
                "('科技产品介绍', '智能产品功能演示视频', 'https://www.w3schools.com/html/mov_bbb.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g4\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%231e1b4b;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23312e81;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g4)\" width=\"400\" height=\"225\"/><rect x=\"140\" y=\"70\" width=\"120\" height=\"80\" rx=\"12\" fill=\"none\" stroke=\"%23818cf8\" stroke-width=\"2\"/><circle cx=\"200\" cy=\"110\" r=\"25\" fill=\"none\" stroke=\"%23a5b4fc\" stroke-width=\"2\"/><line x1=\"200\" y1=\"85\" x2=\"200\" y2=\"135\" stroke=\"%23c7d2fe\" stroke-width=\"2\"/><line x1=\"175\" y1=\"110\" x2=\"225\" y2=\"110\" stroke=\"%23c7d2fe\" stroke-width=\"2\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23c7d2fe\" font-size=\"16\" font-weight=\"bold\">科技前沿</text></svg>', '科技', NULL), " +
                "('历史文化', '中国传统文化介绍视频', 'https://www.w3schools.com/html/movie.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g5\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%237c2d12;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23431407;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g5)\" width=\"400\" height=\"225\"/><polygon points=\"200,50 250,100 250,170 150,170 150,100\" fill=\"%23d97706\"/><rect x=\"180\" y=\"130\" width=\"40\" height=\"40\" fill=\"%2378350f\"/><circle cx=\"200\" cy=\"80\" r=\"15\" fill=\"%23fbbf24\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23fed7aa\" font-size=\"16\" font-weight=\"bold\">历史人文</text></svg>', '文化', NULL), " +
                "('动物世界', '野生动物纪录片片段', 'https://www.w3schools.com/html/mov_bbb.mp4', 'data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 225\"><defs><linearGradient id=\"g6\" x1=\"0%\" y1=\"0%\" x2=\"100%\" y2=\"100%\"><stop offset=\"0%\" style=\"stop-color:%2314532d;stop-opacity:1\" /><stop offset=\"100%\" style=\"stop-color:%23052e16;stop-opacity:1\" /></linearGradient></defs><rect fill=\"url(%23g6)\" width=\"400\" height=\"225\"/><ellipse cx=\"200\" cy=\"130\" rx=\"50\" ry=\"35\" fill=\"%23a16207\"/><circle cx=\"170\" cy=\"115\" r=\"8\" fill=\"%231c1917\"/><circle cx=\"230\" cy=\"115\" r=\"8\" fill=\"%231c1917\"/><ellipse cx=\"200\" cy=\"145\" rx=\"15\" ry=\"8\" fill=\"%2378350f\"/><text x=\"50%\" y=\"90%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"%23bbf7d0\" font-size=\"16\" font-weight=\"bold\">动物世界</text></svg>', '动物', NULL)");

        // 预设数字人形象配置
        stmt.execute("INSERT INTO dh_avatar_config (user_id, name, gender, avatar_style, skin_color, hair_color, clothing_color, background_color, avatar_url) VALUES " +
                "(1, '知性女教师', 'female', 'avataaars', 'ffdbac', '4a312c', '1e40af', 'e0e7ff', 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher&skinColor=ffdbac&hairColor=4a312c&backgroundColor=e0e7ff&clothingColor=1e40af'), " +
                "(1, '专业男主播', 'male', 'avataaars', 'ffdbac', '2c1b18', '166534', 'dcfce7', 'https://api.dicebear.com/7.x/avataaars/svg?seed=host&skinColor=ffdbac&hairColor=2c1b18&backgroundColor=dcfce7&clothingColor=166534'), " +
                "(1, '可爱卡通少女', 'female', 'micah', 'ffdbac', 'f472b6', 'ec4899', 'fce7f3', 'https://api.dicebear.com/7.x/micah/svg?seed=cute&skinColor=ffdbac&hairColor=f472b6&backgroundColor=fce7f3&clothingColor=ec4899')");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DBConfig.getDataSource().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
