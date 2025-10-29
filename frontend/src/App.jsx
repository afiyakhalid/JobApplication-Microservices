import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';

function HomeRedirect() {
  const { user } = useAuth();
  const location = useLocation();
  // redirect to dashboard if logged in, otherwise to login
  return user ? <Navigate to="/dashboard" replace state={{ from: location }} /> : <Navigate to="/login" replace />;
}

function RequireAuth({ children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  return children;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeRedirect />} />
          <Route path="/login" element={<Login />} />
          <Route
            path="/dashboard/*"
            element={
              <RequireAuth>
                <Dashboard />
              </RequireAuth>
            }
          />
          <Route path="*" element={<div style={{ padding: 20 }}>404 - Not Found</div>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}