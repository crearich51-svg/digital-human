import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Row, Col, Button, Typography, Empty, Tag } from 'antd';
import {
  UserOutlined,
  VideoCameraOutlined,
  PlayCircleOutlined,
  PlusOutlined,
  EyeOutlined,
  UserAddOutlined,
  SoundOutlined,
  VideoCameraAddOutlined,
  RocketOutlined,
  StarOutlined,
} from '@ant-design/icons';
import { getAvatars, getVideos, getWorks } from '../services/api';
import type { AvatarConfig, VideoMaterial, Work } from '../types';

const { Title, Text } = Typography;

const Home = () => {
  const navigate = useNavigate();
  const [avatars, setAvatars] = useState<AvatarConfig[]>([]);
  const [videos, setVideos] = useState<VideoMaterial[]>([]);
  const [works, setWorks] = useState<Work[]>([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    try {
      const [avatarRes, videoRes, workRes] = await Promise.all([
        getAvatars(),
        getVideos(),
        getWorks(),
      ]);
      if (avatarRes.code === 200) setAvatars(avatarRes.data.slice(0, 4));
      if (videoRes.code === 200) setVideos(videoRes.data.slice(0, 4));
      if (workRes.code === 200) setWorks(workRes.data.slice(0, 4));
    } catch (error) {
      console.error('加载数据失败:', error);
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
    // 使用 requestAnimationFrame 避免同步调用 setState 导致的级联渲染
    const timer = requestAnimationFrame(() => {
      loadData();
    });

    return () => cancelAnimationFrame(timer);
  }, [navigate]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-8 w-8 border-3 border-indigo-600 border-t-transparent" />
      </div>
    );
  }

  const features = [
    {
      icon: <UserAddOutlined className="text-2xl" />,
      title: '形象定制',
      desc: '打造专属数字人',
      gradient: 'from-blue-500 to-indigo-600',
      bgGradient: 'from-blue-50 to-indigo-50',
      onClick: () => navigate('/avatar'),
    },
    {
      icon: <SoundOutlined className="text-2xl" />,
      title: '语音合成',
      desc: '男女声自由切换',
      gradient: 'from-green-500 to-emerald-600',
      bgGradient: 'from-green-50 to-emerald-50',
      onClick: () => navigate('/create'),
    },
    {
      icon: <VideoCameraAddOutlined className="text-2xl" />,
      title: '视频生成',
      desc: '一键合成讲解视频',
      gradient: 'from-orange-500 to-amber-600',
      bgGradient: 'from-orange-50 to-amber-50',
      onClick: () => navigate('/create'),
    },
  ];

  const stats = [
    {
      title: '我的形象',
      value: avatars.length,
      icon: <UserOutlined />,
      gradient: 'from-blue-500 to-indigo-600',
      bgGradient: 'from-blue-50 to-indigo-100',
    },
    {
      title: '视频素材',
      value: videos.length,
      icon: <VideoCameraOutlined />,
      gradient: 'from-green-500 to-emerald-600',
      bgGradient: 'from-green-50 to-emerald-100',
    },
    {
      title: '作品数量',
      value: works.length,
      icon: <PlayCircleOutlined />,
      gradient: 'from-orange-500 to-amber-600',
      bgGradient: 'from-orange-50 to-amber-100',
    },
  ];

  return (
    <div className="space-y-6 pl-5">
      {/* Hero 区域 */}
      <div className="flex flex-col md:flex-row items-center justify-between gap-8 py-12">
        {/* 左侧文案和按钮 */}
        <div className="flex-1 flex flex-col items-center md:items-start justify-center space-y-3 text-center md:text-left">
          <div className="flex items-center gap-2">
            <StarOutlined className="text-indigo-500 text-xl" />
            <Tag color="blue" className="!m-0">
              智能创作平台
            </Tag>
          </div>
          <Title level={3} className="!text-gray-800 !mb-0 !font-semibold">
            欢迎使用数字人讲解员
          </Title>
          <Text className="text-gray-500 text-lg block">
            定制数字人形象，输入文案，一键生成专业讲解视频
          </Text>
          <div className="flex flex-wrap gap-3 pt-[18px] justify-center md:justify-start">
            <Button
              size="large"
              type="primary"
              onClick={() => navigate('/avatar')}
              className="!font-semibold !px-6 !rounded-xl hover:!scale-105 transition-all duration-200 !shadow-md"
            >
              <UserOutlined /> 定制形象
            </Button>
            <Button
              size="large"
              onClick={() => navigate('/create')}
              className="!font-semibold !px-6 !rounded-xl hover:!scale-105 transition-all duration-200"
            >
              <RocketOutlined /> 开始创作
            </Button>
          </div>
        </div>

        {/* 右侧统计数据 */}
        <div className="flex-1 grid grid-cols-3 gap-6 justify-items-center">
          {stats.map((stat, index) => (
            <div
              key={index}
              className="bg-transparent rounded-none p-4 border border-transparent w-full"
            >
              <div className="text-center">
                <div className="text-gray-500 text-sm">{stat.title}</div>
                <div className="text-3xl font-bold text-gray-800 mt-1">{stat.value}</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 功能入口 */}
      <div className="!mt-5">
        <Row gutter={[16, 16]}>
          {features.map((feature, index) => (
            <Col xs={24} md={8} key={index}>
              <Card
                hoverable
                bordered={false}
                className="h-full cursor-pointer group overflow-hidden !rounded-2xl !shadow-md hover:!shadow-xl transition-all duration-300"
                onClick={feature.onClick}
              >
                <div className="relative">
                  {/* 背景装饰 */}
                  <div className={`absolute -top-10 -right-10 w-32 h-32 rounded-full bg-gradient-to-br ${feature.bgGradient} opacity-60 group-hover:opacity-80 transition-opacity`} />

                  <div className="relative z-10 py-4">
                    <div className={`w-16 h-16 rounded-2xl bg-gradient-to-br ${feature.gradient} flex items-center justify-center text-white shadow-lg mb-4 group-hover:scale-110 group-hover:rotate-3 transition-all duration-300`}>
                      {feature.icon}
                    </div>
                    <Title level={5} className="!mb-2 !font-semibold group-hover:text-indigo-600 transition-colors">
                      {feature.title}
                    </Title>
                    <Text type="secondary" className="text-sm">
                      {feature.desc}
                    </Text>
                    <div className="mt-4 flex items-center gap-1 text-indigo-600 text-sm font-medium opacity-0 group-hover:opacity-100 transition-opacity">
                      立即体验 <span className="group-hover:translate-x-1 transition-transform">→</span>
                    </div>
                  </div>
                </div>
              </Card>
            </Col>
          ))}
        </Row>
      </div>

      {/* 我的数字人 */}
      <Card
        bordered={false}
        className="!rounded-2xl !shadow-md w-full overflow-hidden !mt-5"
        title={
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white">
              <UserOutlined />
            </div>
            <span className="font-semibold">我的数字人</span>
          </div>
        }
        extra={
          <Button type="link" onClick={() => navigate('/avatar')} className="!text-indigo-600">
            查看全部 →
          </Button>
        }
      >
        {avatars.length > 0 ? (
          <div className="card-grid">
            {avatars.map((avatar) => (
              <Card
                key={avatar.id}
                hoverable
                bordered={false}
                className="cursor-pointer w-full !rounded-xl !shadow-sm hover:!shadow-md transition-all duration-200 overflow-hidden group"
                onClick={() => navigate('/avatar')}
                cover={
                  <div
                    className="aspect-square flex items-center justify-center p-4 w-full overflow-hidden"
                    style={{ backgroundColor: `#${avatar.backgroundColor}` }}
                  >
                    <img
                      src={avatar.avatarUrl}
                      alt={avatar.name}
                      className="w-full h-full object-contain group-hover:scale-110 transition-transform duration-300"
                      loading="lazy"
                      style={{ borderRadius: 0 }}
                      onError={(e) => {
                        (e.target as HTMLImageElement).style.display = 'none';
                      }}
                    />
                  </div>
                }
              >
                <Card.Meta
                  title={<div className="text-sm font-semibold truncate">{avatar.name}</div>}
                  description={
                    <Tag color={avatar.gender === 'male' ? 'blue' : 'pink'} className="!m-0">
                      {avatar.gender === 'male' ? '男' : '女'}
                    </Tag>
                  }
                />
              </Card>
            ))}
          </div>
        ) : (
          <Empty
            description="还没有数字人形象"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => navigate('/avatar')}>
              <PlusOutlined /> 创建第一个
            </Button>
          </Empty>
        )}
      </Card>

      {/* 视频素材 */}
      <Card
        bordered={false}
        className="!rounded-2xl !shadow-md w-full overflow-hidden"
        title={
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-green-500 to-emerald-600 flex items-center justify-center text-white">
              <VideoCameraOutlined />
            </div>
            <span className="font-semibold">视频素材</span>
          </div>
        }
        extra={
          <Button type="link" onClick={() => navigate('/create')} className="!text-indigo-600">
            查看全部 →
          </Button>
        }
      >
        {videos.length > 0 ? (
          <div className="card-grid">
            {videos.map((video) => (
              <Card
                key={video.id}
                hoverable
                bordered={false}
                className="cursor-pointer w-full !rounded-xl !shadow-sm hover:!shadow-md transition-all duration-200 overflow-hidden group"
                onClick={() => navigate('/create')}
                cover={
                  <div className="aspect-video relative w-full overflow-hidden bg-gray-100">
                    <img
                      src={video.thumbnail}
                      alt={video.title}
                      className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                      loading="lazy"
                      style={{ borderRadius: 0 }}
                      onError={(e) => {
                        (e.target as HTMLImageElement).src = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 225"><rect fill="%23e2e8f0" width="400" height="225"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%2394a3b8" font-size="14">视频</text></svg>';
                      }}
                    />
                    <Tag color="blue" className="!absolute !top-2 !left-2 !m-0">
                      {video.category}
                    </Tag>
                    <div className="absolute inset-0 flex items-center justify-center bg-black/30 opacity-0 group-hover:opacity-100 transition-opacity">
                      <div className="w-12 h-12 rounded-full bg-white/90 flex items-center justify-center shadow-lg">
                        <PlayCircleOutlined className="text-2xl text-indigo-600 ml-0.5" />
                      </div>
                    </div>
                  </div>
                }
              >
                <Card.Meta
                  title={<div className="text-sm font-semibold truncate">{video.title}</div>}
                  description={<Text type="secondary" className="text-xs line-clamp-2">{video.description}</Text>}
                />
              </Card>
            ))}
          </div>
        ) : (
          <Empty
            description="还没有视频素材"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        )}
      </Card>

      {/* 最近作品 */}
      <Card
        bordered={false}
        className="!rounded-2xl !shadow-md w-full overflow-hidden"
        title={
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-orange-500 to-amber-600 flex items-center justify-center text-white">
              <PlayCircleOutlined />
            </div>
            <span className="font-semibold">最近作品</span>
          </div>
        }
        extra={
          <Button type="link" onClick={() => navigate('/works')} className="!text-indigo-600">
            查看全部 →
          </Button>
        }
      >
        {works.length > 0 ? (
          <div className="card-grid">
            {works.map((work) => (
              <Card
                key={work.id}
                hoverable
                bordered={false}
                className="cursor-pointer w-full !rounded-xl !shadow-sm hover:!shadow-md transition-all duration-200 overflow-hidden group"
                onClick={() => navigate(`/player/${work.id}`)}
                cover={
                  <div className="aspect-video relative w-full overflow-hidden bg-gray-100">
                    <img
                      src={work.thumbnail}
                      alt={work.title}
                      className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                      loading="lazy"
                      style={{ borderRadius: 0 }}
                      onError={(e) => {
                        (e.target as HTMLImageElement).src = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 225"><rect fill="%23e2e8f0" width="400" height="225"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%2394a3b8" font-size="14">视频</text></svg>';
                      }}
                    />
                    <div className="absolute inset-0 flex items-center justify-center bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity">
                      <div className="w-12 h-12 rounded-full bg-white/90 flex items-center justify-center shadow-lg">
                        <PlayCircleOutlined className="text-2xl text-indigo-600 ml-0.5" />
                      </div>
                    </div>
                    {work.avatarUrl && (
                      <div className="absolute bottom-2 right-2 w-8 h-8 rounded-full overflow-hidden border-2 border-white bg-white shadow-md">
                        <img
                          src={work.avatarUrl}
                          alt="数字人"
                          className="w-full h-full object-contain p-0.5"
                          loading="lazy"
                          onError={(e) => {
                            (e.target as HTMLImageElement).style.display = 'none';
                          }}
                        />
                      </div>
                    )}
                  </div>
                }
              >
                <Card.Meta
                  title={<div className="text-sm font-semibold truncate">{work.title}</div>}
                  description={
                    <div className="flex items-center justify-between mt-1">
                      <span className="text-xs text-gray-400 flex items-center gap-1">
                        <EyeOutlined /> {work.views || 0}
                      </span>
                      <span className="text-xs text-gray-400">
                        {new Date(work.createTime!).toLocaleDateString('zh-CN')}
                      </span>
                    </div>
                  }
                />
              </Card>
            ))}
          </div>
        ) : (
          <Empty
            description="还没有作品"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => navigate('/create')}>
              <PlusOutlined /> 创作第一个
            </Button>
          </Empty>
        )}
      </Card>
    </div>
  );
};

export default Home;
