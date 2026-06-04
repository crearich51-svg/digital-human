import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Input,
  Button,
  Radio,
  List,
  Avatar,
  Progress,
  Typography,
  message,
} from 'antd';
import {
  PlayCircleOutlined,
  PauseCircleOutlined,
  SoundOutlined,
  VideoCameraOutlined,
  PlusOutlined,
} from '@ant-design/icons';
import { getAvatars, getVideos, createTask, getTask } from '../services/api';
import { speak, stop, getVoices } from '../utils/speech';
import type { AvatarConfig, VideoMaterial, Task } from '../types';

const { Title, Text } = Typography;
const { TextArea } = Input;

const Create = () => {
  const navigate = useNavigate();
  const [avatars, setAvatars] = useState<AvatarConfig[]>([]);
  const [videos, setVideos] = useState<VideoMaterial[]>([]);
  const [selectedAvatar, setSelectedAvatar] = useState<AvatarConfig | null>(null);
  const [selectedVideo, setSelectedVideo] = useState<VideoMaterial | null>(null);
  const [voiceType, setVoiceType] = useState<'male' | 'female'>('female');
  const [scriptContent, setScriptContent] = useState('');
  const [title, setTitle] = useState('');
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);
  const [currentTask, setCurrentTask] = useState<Task | null>(null);
  const [isSpeaking, setIsSpeaking] = useState(false);
  const [isPlaying, setIsPlaying] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const avatarRef = useRef<HTMLDivElement>(null);
  const pollIntervalRef = useRef<number | null>(null);

  const loadData = async () => {
    try {
      const [avatarRes, videoRes] = await Promise.all([getAvatars(), getVideos()]);
      if (avatarRes.code === 200) {
        setAvatars(avatarRes.data);
        if (avatarRes.data.length > 0) setSelectedAvatar(avatarRes.data[0]);
      }
      if (videoRes.code === 200) {
        setVideos(videoRes.data);
        if (videoRes.data.length > 0) setSelectedVideo(videoRes.data[0]);
      }
    } catch (error) {
      console.error('加载失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const user = localStorage.getItem('user');
    if (!user) {
      navigate('/login');
      return;
    }
    loadData();
    getVoices();
    return () => {
      stop();
      if (pollIntervalRef.current) clearInterval(pollIntervalRef.current);
    };
  }, [navigate]);

  const handlePreviewVoice = () => {
    if (isSpeaking) {
      stop();
      setIsSpeaking(false);
      if (avatarRef.current) avatarRef.current.classList.remove('talking');
      return;
    }
    if (!scriptContent.trim()) {
      message.warning('请输入讲解文案');
      return;
    }
    const utterance = speak({
      text: scriptContent,
      voiceType,
      lang: 'zh-CN',
      onStart: () => {
        setIsSpeaking(true);
        if (avatarRef.current) avatarRef.current.classList.add('talking');
      },
      onEnd: () => {
        setIsSpeaking(false);
        if (avatarRef.current) avatarRef.current.classList.remove('talking');
      },
    });
    if (!utterance) message.error('浏览器不支持语音合成，请使用 Chrome 或 Edge');
  };

  const handleToggleVideo = () => {
    if (!videoRef.current) return;
    if (isPlaying) videoRef.current.pause();
    else videoRef.current.play();
    setIsPlaying(!isPlaying);
  };

  const handleGenerate = async () => {
    if (!selectedAvatar || !selectedVideo || !title.trim() || !scriptContent.trim()) {
      message.warning('请填写完整信息');
      return;
    }
    setGenerating(true);
    try {
      const res = await createTask({
        scriptTitle: title,
        scriptContent: scriptContent,
        avatarConfigId: selectedAvatar.id,
        videoMaterialId: selectedVideo.id,
        voiceType,
      });
      if (res.code === 200) {
        setCurrentTask(res.data);
        startPolling(res.data.id!);
      }
    } catch {
      message.error('创建任务失败');
      setGenerating(false);
    }
  };

  const startPolling = (taskId: number) => {
    pollIntervalRef.current = window.setInterval(async () => {
      try {
        const res = await getTask(taskId);
        if (res.code === 200) {
          setCurrentTask(res.data);
          if (res.data.status === 'FINISHED' || res.data.status === 'FAILED') {
            if (pollIntervalRef.current) clearInterval(pollIntervalRef.current);
            setGenerating(false);
            if (res.data.status === 'FINISHED') {
              message.success('生成完成！');
              navigate('/works');
            } else {
              message.error('生成失败，请重试');
            }
          }
        }
      } catch (error) {
        console.error('查询失败:', error);
      }
    }, 2000);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-8 w-8 border-3 border-indigo-600 border-t-transparent" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <Title level={4} className="!mb-1">创作视频</Title>
          <Text type="secondary">选择素材，输入文案，生成数字人讲解视频</Text>
        </div>
      </div>

      <Row gutter={24}>
        {/* 左侧预览区 */}
        <Col xs={24} lg={16} className="space-y-6">
          {/* 视频预览 */}
          <Card bordered={false} className="shadow-sm overflow-hidden w-full">
            <div className="video-wrapper relative aspect-video bg-black rounded-lg overflow-hidden w-full">
              {selectedVideo ? (
                <video
                  ref={videoRef}
                  src={selectedVideo.videoUrl}
                  poster={selectedVideo.thumbnail}
                  className="w-full h-full object-contain"
                  onClick={handleToggleVideo}
                />
              ) : (
                <div className="w-full h-full flex items-center justify-center text-gray-400 text-sm">
                  请选择视频素材
                </div>
              )}
              {/* 数字人叠加 */}
              {selectedAvatar && (
                <div
                  ref={avatarRef}
                  className="absolute bottom-2 right-2 md:bottom-4 md:right-4 w-16 h-16 md:w-24 md:h-24 rounded-full overflow-hidden border-2 md:border-4 border-white shadow-lg"
                  style={{ backgroundColor: `#${selectedAvatar.backgroundColor}` }}
                >
                  <img
                    src={selectedAvatar.avatarUrl}
                    alt={selectedAvatar.name}
                    className="w-full h-full object-contain p-1 md:p-1.5"
                    onError={(e) => {
                      (e.target as HTMLImageElement).style.display = 'none';
                    }}
                  />
                </div>
              )}
              {selectedVideo && (
                <button
                  onClick={handleToggleVideo}
                  className="absolute inset-0 flex items-center justify-center bg-black/30 hover:bg-black/40 transition-colors"
                >
                  <div className="w-12 h-12 md:w-16 md:h-16 rounded-full bg-white/90 flex items-center justify-center shadow-lg">
                    {isPlaying ? (
                      <PauseCircleOutlined className="text-2xl md:text-3xl text-indigo-600" />
                    ) : (
                      <PlayCircleOutlined className="text-2xl md:text-3xl text-indigo-600 ml-0.5" />
                    )}
                  </div>
                </button>
              )}
            </div>
          </Card>

          {/* 文案输入 */}
          <Card bordered={false} className="shadow-sm" title="讲解文案">
            <Input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="作品标题"
              size="large"
              className="mb-4"
            />
            <TextArea
              value={scriptContent}
              onChange={(e) => setScriptContent(e.target.value)}
              rows={5}
              placeholder="请输入讲解文案，数字人将用指定声音朗读..."
              showCount
            />
            <div className="flex items-center justify-between mt-4">
              <Text type="secondary" className="text-sm">
                {scriptContent.length} 字
              </Text>
              <Button
                onClick={handlePreviewVoice}
                icon={<SoundOutlined />}
                type={isSpeaking ? 'primary' : 'default'}
                danger={isSpeaking}
              >
                {isSpeaking ? '停止' : '试听'}
              </Button>
            </div>
          </Card>
        </Col>

        {/* 右侧配置区 */}
        <Col xs={24} lg={8} className="space-y-4">
          {/* 选择数字人 */}
          <Card
            bordered={false}
            className="shadow-sm"
            title="选择数字人"
            extra={
              <Button type="link" size="small" onClick={() => navigate('/avatar')}>
                <PlusOutlined /> 新建
              </Button>
            }
          >
            {avatars.length > 0 ? (
              <List
                size="small"
                dataSource={avatars}
                renderItem={(avatar) => (
                  <List.Item
                    onClick={() => setSelectedAvatar(avatar)}
                    className={`cursor-pointer rounded-lg px-2 !mb-2 ${
                      selectedAvatar?.id === avatar.id
                        ? 'bg-indigo-50 dark:bg-indigo-900/20 border border-indigo-200 dark:border-indigo-800'
                        : 'hover:bg-gray-50 dark:hover:bg-gray-800'
                    }`}
                  >
                    <List.Item.Meta
                      avatar={
                        <Avatar
                          shape="square"
                          size={48}
                          src={avatar.avatarUrl}
                          style={{ backgroundColor: `#${avatar.backgroundColor}` }}
                        />
                      }
                      title={<span className="font-medium text-sm">{avatar.name}</span>}
                      description={
                        <span className="text-xs text-gray-400">
                          {avatar.gender === 'male' ? '男' : '女'}
                        </span>
                      }
                    />
                  </List.Item>
                )}
              />
            ) : (
              <div className="text-center py-4">
                <Text type="secondary" className="text-xs">还没有数字人</Text>
                <br />
                <Button type="link" size="small" onClick={() => navigate('/avatar')}>
                  去创建 →
                </Button>
              </div>
            )}
          </Card>

          {/* 选择视频 */}
          <Card bordered={false} className="shadow-sm" title="选择视频">
            <List
              size="small"
              dataSource={videos}
              renderItem={(video) => (
                <List.Item
                  onClick={() => setSelectedVideo(video)}
                  className={`cursor-pointer rounded-lg px-2 !mb-2 ${
                    selectedVideo?.id === video.id
                      ? 'bg-indigo-50 dark:bg-indigo-900/20 border border-indigo-200 dark:border-indigo-800'
                      : 'hover:bg-gray-50 dark:hover:bg-gray-800'
                  }`}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        shape="square"
                        size={48}
                        src={video.thumbnail}
                        icon={<VideoCameraOutlined />}
                      />
                    }
                    title={<span className="font-medium text-sm truncate">{video.title}</span>}
                    description={
                      <span className="text-xs text-gray-400 truncate">{video.category}</span>
                    }
                  />
                </List.Item>
              )}
            />
          </Card>

          {/* 选择声音 */}
          <Card bordered={false} className="shadow-sm" title="选择声音">
            <Radio.Group
              value={voiceType}
              onChange={(e) => setVoiceType(e.target.value)}
              className="w-full"
            >
              <Radio.Button value="female" className="w-1/2 text-center">
                👩 女声
              </Radio.Button>
              <Radio.Button value="male" className="w-1/2 text-center">
                👨 男声
              </Radio.Button>
            </Radio.Group>
          </Card>

          {/* 生成按钮 */}
          <Button
            type="primary"
            size="large"
            block
            onClick={handleGenerate}
            loading={generating}
            disabled={!selectedAvatar || !selectedVideo || !title.trim() || !scriptContent.trim()}
            className="h-12"
          >
            {generating ? (
              <span>生成中 {currentTask?.progress || 0}%</span>
            ) : (
              <span>
                <VideoCameraOutlined /> 生成视频
              </span>
            )}
          </Button>

          {/* 进度 */}
          {generating && currentTask && (
            <Card bordered={false} className="shadow-sm">
              <Progress percent={currentTask.progress} status="active" size="small" />
              <Text type="secondary" className="text-xs mt-2 block">
                {currentTask.status === 'PROCESSING' ? '处理中...' : '排队中...'}
              </Text>
            </Card>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default Create;
