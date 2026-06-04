import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Button,
  Typography,
  Avatar,
  Tag,
  Space,
  Alert,
  message,
} from 'antd';
import {
  ArrowLeftOutlined,
  PlayCircleOutlined,
  PauseCircleOutlined,
  VideoCameraOutlined,
  UserOutlined,
  BulbOutlined,
} from '@ant-design/icons';
import { getWork, incrementViews } from '../services/api';
import { speak, stop } from '../utils/speech';
import type { Work } from '../types';

const { Title, Text, Paragraph } = Typography;

const Player = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [work, setWork] = useState<Work | null>(null);
  const [loading, setLoading] = useState(true);
  const [isPlaying, setIsPlaying] = useState(false);
  const [isSpeaking, setIsSpeaking] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const avatarRef = useRef<HTMLDivElement>(null);
  const viewedRef = useRef(false);

  const loadWork = async () => {
    if (!id) return;
    try {
      const res = await getWork(parseInt(id));
      if (res.code === 200) {
        setWork(res.data);
        if (!viewedRef.current) {
          viewedRef.current = true;
          incrementViews(parseInt(id));
        }
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
    loadWork();
    return () => stop();
  }, [id, navigate]);

  const handleToggleVideo = async () => {
    if (!videoRef.current) return;
    if (isPlaying) {
      videoRef.current.pause();
      stop();
      setIsSpeaking(false);
      if (avatarRef.current) avatarRef.current.classList.remove('talking');
      setIsPlaying(false);
    } else {
      try {
        // 先播放视频
        await videoRef.current.play();
        setIsPlaying(true);

        // 然后播放语音（如果有文案和声音类型）
        if (work?.scriptContent) {
          const voiceType = (work.voiceType as 'male' | 'female') || 'female';
          console.log('开始播放语音:', { text: work.scriptContent, voiceType });

          const utterance = speak({
            text: work.scriptContent,
            voiceType,
            lang: 'zh-CN',
            onStart: () => {
              console.log('语音开始播放');
              setIsSpeaking(true);
              if (avatarRef.current) avatarRef.current.classList.add('talking');
            },
            onEnd: () => {
              console.log('语音播放结束');
              setIsSpeaking(false);
              if (avatarRef.current) avatarRef.current.classList.remove('talking');
            },
            onError: (e) => {
              console.error('语音播放错误:', e);
              setIsSpeaking(false);
              if (avatarRef.current) avatarRef.current.classList.remove('talking');
            },
          });

          if (!utterance) {
            console.warn('浏览器不支持语音合成，请使用 Chrome 或 Edge 浏览器');
            message.warning('浏览器不支持语音合成，请使用 Chrome 或 Edge 浏览器');
          }
        } else {
          console.warn('作品没有文案内容，无法播放语音');
        }
      } catch (error) {
        console.error('播放失败:', error);
        message.error('播放失败，请刷新页面重试');
        setIsPlaying(false);
      }
    }
  };

  const handleVideoEnded = () => {
    setIsPlaying(false);
    stop();
    setIsSpeaking(false);
    if (avatarRef.current) avatarRef.current.classList.remove('talking');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-8 w-8 border-3 border-indigo-600 border-t-transparent" />
      </div>
    );
  }

  if (!work) {
    return (
      <div className="text-center py-16">
        <Alert type="warning" message="作品不存在" className="max-w-md mx-auto" />
        <Button className="mt-4" onClick={() => navigate('/works')}>
          返回作品列表
        </Button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate('/works')}
      >
        返回作品列表
      </Button>

      <Row gutter={24}>
        {/* 左侧 */}
        <Col xs={24} lg={16} className="space-y-6">
          {/* 视频 */}
          <Card bordered={false} className="shadow-sm overflow-hidden p-0 w-full">
            <div className="video-wrapper relative aspect-video bg-black w-full">
              <video
                ref={videoRef}
                src={work.videoUrl}
                poster={work.thumbnail}
                className="w-full h-full object-contain"
                onClick={handleToggleVideo}
                onEnded={handleVideoEnded}
              />
              {work.avatarUrl && (
                <div
                  ref={avatarRef}
                  className="absolute bottom-2 right-2 md:bottom-4 md:right-4 w-20 h-20 md:w-28 md:h-28 rounded-full overflow-hidden border-2 md:border-4 border-white shadow-lg"
                  style={{
                    backgroundColor: work.avatarUrl.includes('backgroundColor')
                      ? 'transparent'
                      : '#f0f0f0',
                  }}
                >
                  <img
                    src={work.avatarUrl}
                    alt="数字人"
                    className="w-full h-full object-contain p-1 md:p-2"
                    onError={(e) => {
                      (e.target as HTMLImageElement).style.display = 'none';
                    }}
                  />
                </div>
              )}
              {!isPlaying && (
                <button
                  onClick={handleToggleVideo}
                  className="absolute inset-0 flex items-center justify-center bg-black/40 hover:bg-black/50 transition-colors"
                >
                  <div className="w-12 h-12 md:w-16 md:h-16 rounded-full bg-white/90 flex items-center justify-center shadow-lg">
                    <PlayCircleOutlined className="text-2xl md:text-3xl text-indigo-600 ml-0.5" />
                  </div>
                </button>
              )}
              {isSpeaking && (
                <Tag color="blue" className="absolute top-2 left-2 md:top-4 md:left-4">
                  <span className="flex items-center gap-1">
                    <span className="w-1 h-3 bg-blue-500 rounded-full animate-pulse" />
                    正在讲解
                  </span>
                </Tag>
              )}
            </div>
          </Card>

          {/* 作品信息 */}
          <Card bordered={false} className="shadow-sm">
            <Title level={4} className="!mb-3">{work.title}</Title>
            <Space className="mb-4" wrap>
              <Tag icon={<span>👁️</span>}>{work.views || 0} 次观看</Tag>
              <Tag icon={<span>🎤</span>}>{work.voiceType === 'male' ? '男声' : '女声'}</Tag>
              <Tag icon={<span>📅</span>}>
                {work.createTime ? new Date(work.createTime).toLocaleDateString('zh-CN') : ''}
              </Tag>
            </Space>
            {work.scriptContent && (
              <div className="pt-4 border-t border-gray-100">
                <Title level={5} className="!mb-3">讲解文案</Title>
                <div className="bg-gray-50 dark:bg-gray-800 rounded-lg p-4">
                  <Paragraph className="!mb-0 whitespace-pre-wrap text-gray-700 dark:text-gray-300">
                    {work.scriptContent}
                  </Paragraph>
                </div>
              </div>
            )}
          </Card>
        </Col>

        {/* 右侧 */}
        <Col xs={24} lg={8} className="space-y-4">
          {/* 数字人 */}
          <Card bordered={false} className="shadow-sm" title="数字人讲解员">
            {work.avatarUrl && (
              <div className="flex flex-col items-center">
                <Avatar
                  size={96}
                  src={work.avatarUrl}
                  style={{ backgroundColor: '#f0f0f0' }}
                  icon={<UserOutlined />}
                />
                <Text className="mt-3 font-medium">
                  {work.voiceType === 'male' ? '👨 男声讲解员' : '👩 女声讲解员'}
                </Text>
              </div>
            )}
          </Card>

          {/* 操作 */}
          <div className="space-y-3">
            <Button
              type={isPlaying ? 'default' : 'primary'}
              danger={isPlaying}
              size="large"
              block
              onClick={handleToggleVideo}
              icon={isPlaying ? <PauseCircleOutlined /> : <PlayCircleOutlined />}
            >
              {isPlaying ? '暂停播放' : '开始播放'}
            </Button>
            <Button
              size="large"
              block
              onClick={() => navigate('/create')}
              icon={<VideoCameraOutlined />}
            >
              创作新作品
            </Button>
            <Button
              size="large"
              block
              onClick={() => navigate('/avatar')}
              icon={<UserOutlined />}
            >
              定制新形象
            </Button>
          </div>

          {/* 提示 */}
          <Alert
            icon={<BulbOutlined />}
            type="info"
            showIcon
            message="播放说明"
            description={
              <ul className="text-sm space-y-1 mt-1 pl-0 list-none">
                <li>• 点击视频开始/暂停</li>
                <li>• 播放时数字人同步讲解</li>
                <li>• 数字人说话时有动画效果</li>
              </ul>
            }
          />
        </Col>
      </Row>
    </div>
  );
};

export default Player;
