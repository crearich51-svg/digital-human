import { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Form,
  Input,
  Button,
  Radio,
  Space,
  Typography,
  Empty,
  Popconfirm,
  message,
  Drawer,
} from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, CloseOutlined } from '@ant-design/icons';
import { getAvatars, createAvatar, updateAvatar, deleteAvatar } from '../services/api';
import type { AvatarConfig, ColorOption } from '../types';

const { Title, Text } = Typography;

const avatarStyles = [
  { value: 'avataaars', label: '卡通' },
  { value: 'lorelei', label: '洛蕾莱' },
  { value: 'micah', label: '米卡' },
  { value: 'miniavs', label: '迷你' },
  { value: 'bottts', label: '机器人' },
  { value: 'adventurer', label: '冒险家' },
];

const skinColors: ColorOption[] = [
  { name: '白皙', value: 'ffdbb4' },
  { name: '自然', value: 'e8bb96' },
  { name: '小麦', value: 'd4a574' },
  { name: '健康', value: 'c68642' },
];

const hairColors: ColorOption[] = [
  { name: '黑色', value: '2c1b18' },
  { name: '深棕', value: '4a3728' },
  { name: '金色', value: 'f5d654' },
  { name: '红色', value: 'c83c23' },
  { name: '蓝色', value: '25557c' },
  { name: '紫色', value: '6b3e8e' },
];

const clothingColors: ColorOption[] = [
  { name: '深蓝', value: '1e40af' },
  { name: '浅蓝', value: '3b82f6' },
  { name: '红色', value: 'dc2626' },
  { name: '绿色', value: '16a34a' },
  { name: '紫色', value: '7c3aed' },
  { name: '黑色', value: '1f2937' },
];

const bgColors: ColorOption[] = [
  { name: '天蓝', value: 'bfdbfe' },
  { name: '薄荷', value: 'a7f3d0' },
  { name: '樱花', value: 'fbcfe8' },
  { name: '淡紫', value: 'ddd6fe' },
  { name: '灰色', value: 'e5e7eb' },
];

const ColorPicker = ({
  colors,
  value,
  onChange,
}: {
  colors: ColorOption[];
  value: string;
  onChange: (val: string) => void;
}) => (
  <Space wrap size={[8, 8]}>
    {colors.map((color) => (
      <div
        key={color.value}
        onClick={() => onChange(color.value)}
        className={`w-8 h-8 rounded-lg cursor-pointer transition-all hover:scale-110 border-3 ${
          value === color.value ? 'border-indigo-600 scale-110' : 'border-transparent'
        }`}
        style={{ backgroundColor: `#${color.value}` }}
        title={color.name}
      />
    ))}
  </Space>
);

const AvatarCustomize = () => {
  const navigate = useNavigate();
  const [avatars, setAvatars] = useState<AvatarConfig[]>([]);
  const [selectedAvatar, setSelectedAvatar] = useState<AvatarConfig | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [form] = Form.useForm();

  const [formData, setFormData] = useState<Partial<AvatarConfig>>({
    name: '',
    gender: 'female',
    avatarStyle: 'avataaars',
    skinColor: 'ffdbb4',
    hairColor: '2c1b18',
    clothingColor: '3b82f6',
    backgroundColor: 'bfdbfe',
  });

  const loadAvatars = async () => {
    try {
      const res = await getAvatars();
      if (res.code === 200) setAvatars(res.data);
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
    loadAvatars();
  }, [navigate]);

  const previewUrl = useMemo(() => {
    const baseUrl = `https://api.dicebear.com/7.x/${formData.avatarStyle}/svg?`;
    const params = new URLSearchParams({
      skinColor: formData.skinColor!,
      hairColor: formData.hairColor!,
      clothingColor: formData.clothingColor!,
      backgroundColor: formData.backgroundColor!,
      gender: formData.gender!,
      seed: formData.name || 'preview',
    });
    return baseUrl + params.toString();
  }, [formData]);

  const handleOpenDrawer = (avatar?: AvatarConfig) => {
    if (avatar) {
      setSelectedAvatar(avatar);
      setFormData({
        name: avatar.name,
        gender: avatar.gender,
        avatarStyle: avatar.avatarStyle,
        skinColor: avatar.skinColor,
        hairColor: avatar.hairColor,
        clothingColor: avatar.clothingColor,
        backgroundColor: avatar.backgroundColor,
      });
      form.setFieldsValue({
        name: avatar.name,
        gender: avatar.gender,
        avatarStyle: avatar.avatarStyle,
      });
    } else {
      setSelectedAvatar(null);
      setFormData({
        name: '',
        gender: 'female',
        avatarStyle: 'avataaars',
        skinColor: 'ffdbb4',
        hairColor: '2c1b18',
        clothingColor: '3b82f6',
        backgroundColor: 'bfdbfe',
      });
      form.resetFields();
    }
    setDrawerVisible(true);
  };

  const handleCloseDrawer = () => {
    setDrawerVisible(false);
    setSelectedAvatar(null);
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      if (!formData.name?.trim()) {
        message.error('请输入形象名称');
        return;
      }

      setSaving(true);
      const saveData = { ...formData, ...values };

      if (selectedAvatar) {
        const res = await updateAvatar({ ...saveData, id: selectedAvatar.id } as AvatarConfig);
        if (res.code === 200) {
          message.success('更新成功');
          handleCloseDrawer();
          loadAvatars();
        }
      } else {
        const res = await createAvatar(saveData);
        if (res.code === 200) {
          message.success('创建成功');
          handleCloseDrawer();
          loadAvatars();
        }
      }
    } catch (error) {
      console.error('保存失败:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const res = await deleteAvatar(id);
      if (res.code === 200) {
        message.success('删除成功');
        loadAvatars();
      }
    } catch (error) {
      message.error('删除失败');
    }
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
      {/* 标题 */}
      <div className="flex items-center justify-between">
        <div>
          <Title level={5} className="!mb-1">数字人形象定制</Title>
          <Text type="secondary">调整参数，打造专属数字人</Text>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => handleOpenDrawer()}>
          创建新形象
        </Button>
      </div>

      {/* 形象列表 */}
      {avatars.length > 0 ? (
        <div className="card-grid">
          {avatars.map((avatar) => (
            <Card
              key={avatar.id}
              bordered
              hoverable
              className="w-full"
              cover={
                <div
                  className="aspect-square flex items-center justify-center p-4 w-full"
                  style={{ backgroundColor: `#${avatar.backgroundColor}` }}
                >
                  <img
                    src={avatar.avatarUrl}
                    alt={avatar.name}
                    className="w-full h-full object-contain"
                    loading="lazy"
                    onError={(e) => {
                      (e.target as HTMLImageElement).style.display = 'none';
                    }}
                  />
                </div>
              }
              actions={[
                <EditOutlined key="edit" onClick={() => handleOpenDrawer(avatar)} />,
                <Popconfirm
                  key="delete"
                  title="确定删除？"
                  onConfirm={() => handleDelete(avatar.id!)}
                  okText="确定"
                  cancelText="取消"
                >
                  <DeleteOutlined className="text-red-500" />
                </Popconfirm>,
              ]}
            >
              <Card.Meta
                title={<div className="font-medium truncate">{avatar.name}</div>}
                description={
                  <Text type="secondary" className="text-xs">
                    {avatar.gender === 'male' ? '男' : '女'} · {avatar.avatarStyle}
                  </Text>
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
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleOpenDrawer()}>
            创建第一个
          </Button>
        </Empty>
      )}

      {/* 编辑抽屉 */}
      <Drawer
        title={selectedAvatar ? '编辑形象' : '创建新形象'}
        placement="right"
        width={720}
        onClose={handleCloseDrawer}
        open={drawerVisible}
        extra={
          <Space>
            <Button onClick={handleCloseDrawer} icon={<CloseOutlined />}>
              取消
            </Button>
            <Button type="primary" onClick={handleSave} loading={saving}>
              {selectedAvatar ? '更新' : '保存'}
            </Button>
          </Space>
        }
      >
        <Row gutter={24}>
          {/* 预览 */}
          <Col xs={24} md={10}>
            <div className="sticky top-0">
              <Title level={5} className="!mb-3">实时预览</Title>
              <div
                className="aspect-square rounded-xl flex items-center justify-center p-6 border border-gray-200"
                style={{ backgroundColor: `#${formData.backgroundColor}` }}
              >
                <img
                  src={previewUrl}
                  alt="预览"
                  className="w-full h-full object-contain"
                  onError={(e) => {
                    (e.target as HTMLImageElement).style.display = 'none';
                  }}
                />
              </div>
            </div>
          </Col>

          {/* 配置 */}
          <Col xs={24} md={14}>
            <Form form={form} layout="vertical" initialValues={formData}>
              <Row gutter={16}>
                <Col xs={24} md={12}>
                  <Form.Item
                    name="name"
                    label="形象名称"
                    rules={[{ required: true, message: '请输入形象名称' }]}
                  >
                    <Input
                      placeholder="给数字人起个名字"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    />
                  </Form.Item>
                </Col>
                <Col xs={24} md={12}>
                  <Form.Item
                    name="gender"
                    label="性别"
                    rules={[{ required: true, message: '请选择性别' }]}
                  >
                    <Radio.Group
                      value={formData.gender}
                      onChange={(e) => setFormData({ ...formData, gender: e.target.value })}
                      className="w-full"
                    >
                      <Radio.Button value="female" className="w-1/2 text-center">
                        👩 女性
                      </Radio.Button>
                      <Radio.Button value="male" className="w-1/2 text-center">
                        👨 男性
                      </Radio.Button>
                    </Radio.Group>
                  </Form.Item>
                </Col>
              </Row>

              <Form.Item
                name="avatarStyle"
                label="形象风格"
                rules={[{ required: true, message: '请选择风格' }]}
              >
                <Radio.Group
                  value={formData.avatarStyle}
                  onChange={(e) => setFormData({ ...formData, avatarStyle: e.target.value })}
                >
                  {avatarStyles.map((style) => (
                    <Radio.Button key={style.value} value={style.value}>
                      {style.label}
                    </Radio.Button>
                  ))}
                </Radio.Group>
              </Form.Item>

              <Row gutter={16}>
                <Col xs={12}>
                  <Form.Item label="肤色">
                    <ColorPicker
                      colors={skinColors}
                      value={formData.skinColor!}
                      onChange={(val) => setFormData({ ...formData, skinColor: val })}
                    />
                  </Form.Item>
                </Col>
                <Col xs={12}>
                  <Form.Item label="发色">
                    <ColorPicker
                      colors={hairColors}
                      value={formData.hairColor!}
                      onChange={(val) => setFormData({ ...formData, hairColor: val })}
                    />
                  </Form.Item>
                </Col>
                <Col xs={12}>
                  <Form.Item label="服装颜色">
                    <ColorPicker
                      colors={clothingColors}
                      value={formData.clothingColor!}
                      onChange={(val) => setFormData({ ...formData, clothingColor: val })}
                    />
                  </Form.Item>
                </Col>
                <Col xs={12}>
                  <Form.Item label="背景颜色">
                    <ColorPicker
                      colors={bgColors}
                      value={formData.backgroundColor!}
                      onChange={(val) => setFormData({ ...formData, backgroundColor: val })}
                    />
                  </Form.Item>
                </Col>
              </Row>
            </Form>
          </Col>
        </Row>
      </Drawer>
    </div>
  );
};

export default AvatarCustomize;