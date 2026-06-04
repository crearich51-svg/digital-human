package com.example.digitalhuman.entity;

import java.time.LocalDateTime;

/**
 * 生成任务实体类
 */
public class Task {
    private Long id;
    private Long scriptId;
    private Long avatarConfigId;
    private Long videoMaterialId;
    private String voiceType;
    private String status;
    private Integer progress;
    private String resultUrl;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
    private String scriptTitle;
    private String scriptContent;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getScriptId() { return scriptId; }
    public void setScriptId(Long scriptId) { this.scriptId = scriptId; }
    public Long getAvatarConfigId() { return avatarConfigId; }
    public void setAvatarConfigId(Long avatarConfigId) { this.avatarConfigId = avatarConfigId; }
    public Long getVideoMaterialId() { return videoMaterialId; }
    public void setVideoMaterialId(Long videoMaterialId) { this.videoMaterialId = videoMaterialId; }
    public String getVoiceType() { return voiceType; }
    public void setVoiceType(String voiceType) { this.voiceType = voiceType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public String getResultUrl() { return resultUrl; }
    public void setResultUrl(String resultUrl) { this.resultUrl = resultUrl; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public String getScriptTitle() { return scriptTitle; }
    public void setScriptTitle(String scriptTitle) { this.scriptTitle = scriptTitle; }
    public String getScriptContent() { return scriptContent; }
    public void setScriptContent(String scriptContent) { this.scriptContent = scriptContent; }
}
