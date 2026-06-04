import type {
  ApiResponse,
  User,
  AvatarConfig,
  VideoMaterial,
  Script,
  Task,
  Work,
} from '../types';

const API_BASE = '/api';

// 通用请求方法
async function request<T>(url: string, options: RequestInit = {}): Promise<ApiResponse<T>> {
  const response = await fetch(`${API_BASE}${url}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });
  return response.json();
}

// 登录
export const login = (username: string, password: string): Promise<ApiResponse<User>> => {
  return request<User>('/login', {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });
};

// 数字人形象配置
export const getAvatars = (userId: number = 1): Promise<ApiResponse<AvatarConfig[]>> => {
  return request<AvatarConfig[]>(`/avatars?userId=${userId}`);
};

export const getAvatar = (id: number): Promise<ApiResponse<AvatarConfig>> => {
  return request<AvatarConfig>(`/avatars?id=${id}`);
};

export const createAvatar = (data: Partial<AvatarConfig>): Promise<ApiResponse<AvatarConfig>> => {
  return request<AvatarConfig>('/avatars', {
    method: 'POST',
    body: JSON.stringify(data),
  });
};

export const updateAvatar = (data: AvatarConfig): Promise<ApiResponse<AvatarConfig>> => {
  return request<AvatarConfig>('/avatars', {
    method: 'PUT',
    body: JSON.stringify(data),
  });
};

export const deleteAvatar = (id: number): Promise<ApiResponse<null>> => {
  return request<null>(`/avatars?id=${id}`, {
    method: 'DELETE',
  });
};

// 视频素材
export const getVideos = (): Promise<ApiResponse<VideoMaterial[]>> => {
  return request<VideoMaterial[]>('/videos');
};

export const getVideo = (id: number): Promise<ApiResponse<VideoMaterial>> => {
  return request<VideoMaterial>(`/videos?id=${id}`);
};

export const createVideo = (data: Partial<VideoMaterial>): Promise<ApiResponse<VideoMaterial>> => {
  return request<VideoMaterial>('/videos', {
    method: 'POST',
    body: JSON.stringify(data),
  });
};

// 脚本
export const getScripts = (userId: number = 1): Promise<ApiResponse<Script[]>> => {
  return request<Script[]>(`/scripts?userId=${userId}`);
};

export const createScript = (data: Partial<Script>): Promise<ApiResponse<Script>> => {
  return request<Script>('/scripts', {
    method: 'POST',
    body: JSON.stringify(data),
  });
};

// 生成任务
export const getTasks = (): Promise<ApiResponse<Task[]>> => {
  return request<Task[]>('/tasks');
};

export const getTask = (id: number): Promise<ApiResponse<Task>> => {
  return request<Task>(`/tasks?id=${id}`);
};

export const createTask = (data: Partial<Task>): Promise<ApiResponse<Task>> => {
  return request<Task>('/tasks', {
    method: 'POST',
    body: JSON.stringify(data),
  });
};

// 作品
export const getWorks = (userId: number = 1): Promise<ApiResponse<Work[]>> => {
  return request<Work[]>(`/works?userId=${userId}`);
};

export const getWork = (id: number): Promise<ApiResponse<Work>> => {
  return request<Work>(`/works?id=${id}`);
};

export const incrementViews = (id: number): Promise<ApiResponse<null>> => {
  return request<null>(`/works/view?id=${id}`, {
    method: 'POST',
  });
};

export const deleteWork = (id: number): Promise<ApiResponse<null>> => {
  return request<null>(`/works?id=${id}`, {
    method: 'DELETE',
  });
};
