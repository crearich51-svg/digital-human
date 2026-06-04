package com.example.digitalhuman.entity;

import java.time.LocalDateTime;

/**
 * 声音配置实体类
 */
public class Voice {
    private Long id;
    private String name;
    private String voiceType;
    private String language;
    private String sampleUrl;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVoiceType() { return voiceType; }
    public void setVoiceType(String voiceType) { this.voiceType = voiceType; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getSampleUrl() { return sampleUrl; }
    public void setSampleUrl(String sampleUrl) { this.sampleUrl = sampleUrl; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
