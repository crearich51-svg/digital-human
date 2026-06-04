package com.example.digitalhuman.entity;

import java.time.LocalDateTime;

/**
 * 作品实体类
 */
public class Work {
    private Long id;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private Long scriptId;
    private Long avatarConfigId;
    private Long videoMaterialId;
    private String voiceType;
    private Long userId;
    private Integer views;
    private LocalDateTime createTime;
    private String scriptContent;
    private String avatarUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public Long getScriptId() { return scriptId; }
    public void setScriptId(Long scriptId) { this.scriptId = scriptId; }
    public Long getAvatarConfigId() { return avatarConfigId; }
    public void setAvatarConfigId(Long avatarConfigId) { this.avatarConfigId = avatarConfigId; }
    public Long getVideoMaterialId() { return videoMaterialId; }
    public void setVideoMaterialId(Long videoMaterialId) { this.videoMaterialId = videoMaterialId; }
    public String getVoiceType() { return voiceType; }
    public void setVoiceType(String voiceType) { this.voiceType = voiceType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getScriptContent() { return scriptContent; }
    public void setScriptContent(String scriptContent) { this.scriptContent = scriptContent; }
}
