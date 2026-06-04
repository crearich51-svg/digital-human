import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Button, Typography, Empty, Popconfirm, message } from 'antd';
import {
  PlusOutlined,
  PlayCircleOutlined,
  EyeOutlined,
  DeleteOutlined,
} from '@ant-design/icons';
import { getWorks, deleteWork } from '../services/api';
import type { Work } from '../types';

const { Title, Text } = Typography;

const Works = () => {
  const navigate = useNavigate();
  const [works, setWorks] = useState<Work[]>([]);
  const [loading, setLoading] = useState(true);

  const loadWorks = async () => {
    try {
      const res = await getWorks();
      if (res.code === 200) setWorks(res.data);
    } catch {
      console.error('加载失败');
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
    loadWorks();
  }, [navigate]);

  const handleDelete = async (id: number) => {
    try {
      const res = await deleteWork(id);
      if (res.code === 200) {
        message.success('删除成功');
        loadWorks();
      }
    } catch {
      message.error('删除失败');
    }
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleDateString('zh-CN');
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
          <Title level={4} className="!mb-1">我的作品</Title>
          <Text type="secondary">查看和管理创作的视频</Text>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/create')}>
          创作新作品
        </Button>
      </div>

      {works.length > 0 ? (
        <div className="card-grid">
          {works.map((work) => (
            <Card
              key={work.id}
              hoverable
              bordered
              className="cursor-pointer w-full"
              onClick={() => navigate(`/player/${work.id}`)}
              cover={
                <div className="aspect-video relative w-full overflow-hidden bg-gray-100">
                  <img
                    src={work.thumbnail}
                    alt={work.title}
                    className="w-full h-full object-cover"
                    loading="lazy"
                    style={{ borderRadius: 0 }}
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 225"><rect fill="%23e2e8f0" width="400" height="225"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%2394a3b8" font-size="14">视频</text></svg>';
                    }}
                  />
                  <div className="absolute inset-0 flex items-center justify-center bg-black/40 opacity-0 hover:opacity-100 transition-opacity">
                    <div className="w-12 h-12 rounded-full bg-white/90 flex items-center justify-center shadow-lg">
                      <PlayCircleOutlined className="text-2xl text-indigo-600 ml-0.5" />
                    </div>
                  </div>
                  {work.avatarUrl && (
                    <div className="absolute bottom-2 right-2 w-8 h-8 rounded-full overflow-hidden border-2 border-white bg-white">
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
              actions={[
                <Popconfirm
                  key="delete"
                  title="确定删除？"
                  onConfirm={(e) => {
                    e?.stopPropagation();
                    handleDelete(work.id!);
                  }}
                  okText="确定"
                  cancelText="取消"
                >
                  <span
                    onClick={(e) => e.stopPropagation()}
                    className="text-red-500 hover:text-red-600"
                  >
                    <DeleteOutlined /> 删除
                  </span>
                </Popconfirm>,
              ]}
            >
              <Card.Meta
                title={<div className="font-medium truncate">{work.title}</div>}
                description={
                  <div className="flex items-center justify-between mt-1">
                    <span className="text-xs text-gray-400 flex items-center gap-1">
                      <EyeOutlined /> {work.views || 0}
                    </span>
                    <span className="text-xs text-gray-400">{formatDate(work.createTime)}</span>
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
          <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/create')}>
            开始创作
          </Button>
        </Empty>
      )}
    </div>
  );
};

export default Works;
