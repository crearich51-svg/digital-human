import { Link, useNavigate, useLocation, Outlet } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { Layout, Menu, Avatar, Dropdown, Switch } from 'antd';
import {
  HomeOutlined,
  UserOutlined,
  VideoCameraOutlined,
  PlayCircleOutlined,
  LogoutOutlined,
  BulbOutlined,
  BulbFilled,
} from '@ant-design/icons';
import type { User } from '../types';

const { Header, Content, Footer } = Layout;

const AppLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState<User | null>(null);
  const [darkMode, setDarkMode] = useState(false);

  useEffect(() => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      setUser(JSON.parse(userStr));
    } else {
      navigate('/login');
    }
  }, [navigate]);

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [darkMode]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    navigate('/login');
  };

  const menuItems = [
    { key: '/', icon: <HomeOutlined />, label: '首页' },
    { key: '/avatar', icon: <UserOutlined />, label: '形象' },
    { key: '/create', icon: <VideoCameraOutlined />, label: '创作' },
    { key: '/works', icon: <PlayCircleOutlined />, label: '作品' },
  ];

  const userMenuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  return (
    <Layout className="min-h-screen bg-gray-50 dark:bg-gray-900">
      <Header
        className="sticky top-0 z-50 px-4 md:px-6"
        style={{ background: '#fff', borderBottom: '1px solid #f0f0f0' }}
      >
        <div className="flex items-center justify-between h-full max-w-7xl mx-auto">
          <div className="flex items-center gap-8">
            <Link to="/" className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-indigo-600 flex items-center justify-center">
                <span className="text-white text-sm">🎭</span>
              </div>
              <span className="font-semibold text-gray-800 hidden sm:block">数字人讲解员</span>
            </Link>

            <Menu
              mode="horizontal"
              selectedKeys={[location.pathname]}
              onClick={({ key }) => navigate(key)}
              items={menuItems}
              className="border-0 flex-1 min-w-0"
              style={{ minWidth: 'auto' }}
            />
          </div>

          <div className="flex items-center gap-3">
            <Switch
              checked={darkMode}
              onChange={setDarkMode}
              checkedChildren={<BulbFilled />}
              unCheckedChildren={<BulbOutlined />}
              size="small"
            />
            {user && (
              <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
                <div className="flex items-center gap-2 cursor-pointer hover:bg-gray-50 px-2 py-1 rounded-lg transition-colors">
                  <Avatar size="small" icon={<UserOutlined />} />
                  <span className="text-sm text-gray-700 hidden sm:block">{user.nickname}</span>
                </div>
              </Dropdown>
            )}
          </div>
        </div>
      </Header>

      <Content className="flex-1 w-full">
        <div className="w-full max-w-full px-4 md:px-6 py-6 animate-fadeIn">
          <div className="max-w-7xl mx-auto w-full">
            <Outlet />
          </div>
        </div>
      </Content>

      <Footer className="text-center text-gray-500 text-sm py-4 bg-transparent">
        © 2026 数字人智能讲解员
      </Footer>
    </Layout>
  );
};

export default AppLayout;
