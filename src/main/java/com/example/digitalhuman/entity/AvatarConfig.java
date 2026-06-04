package com.example.digitalhuman.entity;

import java.time.LocalDateTime;

/**
 * 数字人形象配置实体类
 * 用户定制的数字人形象参数
 */
public class AvatarConfig {
    private Long id;
    private Long userId;
    private String name;
    private String gender;      // male, female
    private String avatarStyle; // avataaars, bottts, micah, personas
    private String skinColor; // 肤色
    private String hairColor; // 发色
    private String hairStyle; // 发型
    private String eyeStyle; // 眼睛样式
    private String mouthStyle; // 嘴巴样式
    private String clothingColor; // 服装颜色
    private String clothingStyle; // 服装样式
    private String backgroundColor; // 背景色
    private String avatarUrl; // 生成的头像URL
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAvatarStyle() { return avatarStyle; }
    public void setAvatarStyle(String avatarStyle) { this.avatarStyle = avatarStyle; }
    public String getSkinColor() { return skinColor; }
    public void setSkinColor(String skinColor) { this.skinColor = skinColor; }
    public String getHairColor() { return hairColor; }
    public void setHairColor(String hairColor) { this.hairColor = hairColor; }
    public String getHairStyle() { return hairStyle; }
    public void setHairStyle(String hairStyle) { this.hairStyle = hairStyle; }
    public String getEyeStyle() { return eyeStyle; }
    public void setEyeStyle(String eyeStyle) { this.eyeStyle = eyeStyle; }
    public String getMouthStyle() { return mouthStyle; }
    public void setMouthStyle(String mouthStyle) { this.mouthStyle = mouthStyle; }
    public String getClothingColor() { return clothingColor; }
    public void setClothingColor(String clothingColor) { this.clothingColor = clothingColor; }
    public String getClothingStyle() { return clothingStyle; }
    public void setClothingStyle(String clothingStyle) { this.clothingStyle = clothingStyle; }
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    /**
     * 根据参数生成 DiceBear URL
     */
    public String generateAvatarUrl() {
        String seed = name + "_" + id;
        String baseUrl = "https://api.dicebear.com/7.x/" + avatarStyle + "/svg?";
        StringBuilder params = new StringBuilder();
        params.append("seed=").append(seed);
        if (skinColor != null && !skinColor.isEmpty()) {
            params.append("&skinColor=").append(skinColor);
        }
        if (hairColor != null && !hairColor.isEmpty()) {
            params.append("&hairColor=").append(hairColor);
        }
        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            params.append("&backgroundColor=").append(backgroundColor);
        }
        if (clothingColor != null && !clothingColor.isEmpty()) {
            params.append("&clothingColor=").append(clothingColor);
        }
        return baseUrl + params.toString();
    }
}
