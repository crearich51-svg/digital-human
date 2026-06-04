import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';
import Home from './pages/Home';
import AvatarCustomize from './pages/AvatarCustomize';
import Create from './pages/Create';
import Works from './pages/Works';
import Player from './pages/Player';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="avatar" element={<AvatarCustomize />} />
          <Route path="create" element={<Create />} />
          <Route path="works" element={<Works />} />
          <Route path="player/:id" element={<Player />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
