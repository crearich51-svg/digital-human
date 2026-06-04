// 通用响应类型
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 用户类型
export interface User {
  userId: number;
  username: string;
  nickname: string;
}

// 数字人形象配置
export interface AvatarConfig {
  id?: number;
  userId?: number;
  name: string;
  gender: 'male' | 'female';
  avatarStyle: string;
  skinColor: string;
  hairColor: string;
  hairStyle: string;
  eyeStyle: string;
  mouthStyle: string;
  clothingColor: string;
  clothingStyle: string;
  backgroundColor: string;
  avatarUrl?: string;
  createTime?: string;
}

// 视频素材
export interface VideoMaterial {
  id?: number;
  title: string;
  description: string;
  videoUrl: string;
  thumbnail: string;
  category: string;
  userId?: number;
  createTime?: string;
}

// 脚本
export interface Script {
  id?: number;
  title: string;
  content: string;
  characterId?: number;
  voiceId?: number;
  userId?: number;
  characterName?: string;
  voiceName?: string;
  createTime?: string;
}

// 生成任务
export interface Task {
  id?: number;
  scriptId?: number;
  avatarConfigId?: number;
  videoMaterialId?: number;
  voiceType?: string;
  status: 'PENDING' | 'PROCESSING' | 'FINISHED' | 'FAILED';
  progress: number;
  resultUrl?: string;
  scriptTitle?: string;
  scriptContent?: string;
  createTime?: string;
  finishTime?: string;
}

// 作品
export interface Work {
  id?: number;
  title: string;
  videoUrl: string;
  thumbnail: string;
  scriptId?: number;
  avatarConfigId?: number;
  videoMaterialId?: number;
  voiceType?: string;
  userId?: number;
  views?: number;
  createTime?: string;
  scriptContent?: string;
  avatarUrl?: string;
}

// 声音类型
export interface VoiceOption {
  type: 'male' | 'female';
  name: string;
  lang: string;
}

// 预设颜色选项
export interface ColorOption {
  name: string;
  value: string;
}
