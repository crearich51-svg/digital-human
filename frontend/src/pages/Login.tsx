import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button, Card, Alert, Typography } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { login } from '../services/api';
import type { User } from '../types';

const { Title, Text } = Typography;

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [form] = Form.useForm();

  const handleSubmit = async (values: { username: string; password: string }) => {
    setLoading(true);
    setError('');

    try {
      const response = await login(values.username, values.password);
      if (response.code === 200) {
        const user = response.data as User;
        localStorage.setItem('user', JSON.stringify(user));
        navigate('/');
      } else {
        setError(response.message);
      }
    } catch (err) {
      setError('登录失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gradient-to-br from-indigo-50 via-white to-purple-50">
      <Card className="w-full max-w-md shadow-xl" bordered={false}>
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-xl bg-indigo-600 mb-4">
            <span className="text-3xl text-white">🎭</span>
          </div>
          <Title level={3} className="!mb-1">数字人讲解员</Title>
          <Text type="secondary">AI 视频创作平台</Text>
        </div>

        {error && (
          <Alert
            message={error}
            type="error"
            showIcon
            className="mb-4"
          />
        )}

        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          initialValues={{ username: 'admin', password: '123456' }}
        >
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input
              prefix={<UserOutlined className="text-gray-400" />}
              placeholder="请输入用户名"
              size="large"
            />
          </Form.Item>

          <Form.Item
            name="password"
            label="密码"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password
              prefix={<LockOutlined className="text-gray-400" />}
              placeholder="请输入密码"
              size="large"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              size="large"
              block
              loading={loading}
              className="h-11"
            >
              登 录
            </Button>
          </Form.Item>
        </Form>

        <div className="text-center pt-4 border-t border-gray-100">
          <Text type="secondary" className="text-xs">测试账号</Text>
          <div className="flex justify-center gap-4 mt-2">
            <Text type="secondary" className="text-xs">
              账号：<span className="font-mono text-indigo-600">admin</span>
            </Text>
            <Text type="secondary" className="text-xs">
              密码：<span className="font-mono text-indigo-600">123456</span>
            </Text>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default Login;
