package com.example.digitalhuman.entity;

import java.time.LocalDateTime;

/**
 * 脚本实体类
 */
public class Script {
    private Long id;
    private String title;
    private String content;
    private Long characterId;
    private Long voiceId;
    private Long userId;
    private LocalDateTime createTime;
    private String characterName;
    private String voiceName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getCharacterId() { return characterId; }
    public void setCharacterId(Long characterId) { this.characterId = characterId; }
    public Long getVoiceId() { return voiceId; }
    public void setVoiceId(Long voiceId) { this.voiceId = voiceId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public String getVoiceName() { return voiceName; }
    public void setVoiceName(String voiceName) { this.voiceName = voiceName; }
}
