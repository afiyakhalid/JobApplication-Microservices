import React, { createContext, useContext, useState } from 'react';
import axios from '../api/axios';

const AuthCtx = createContext();

export const AuthProvider = ({ children }) => {
  // try to parse stored user (supports both plain string or JSON)
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('user');
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return raw;
    }
  });
  const [loading, setLoading] = useState(false);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const { data } = await axios.post('/auth/login', { username, password });
      // store token and user (store user as JSON string for consistent retrieval)
      localStorage.setItem('token', data.accessToken);
      localStorage.setItem('user', JSON.stringify(username));
      setUser(username);
      setLoading(false);
      return { ok: true };
    } catch (err) {
      setLoading(false);
      const msg = err?.response?.data?.message || err.message || 'Login failed';
      return { ok: false, message: msg };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  return (
    <AuthCtx.Provider value={{ user, setUser, login, logout, loading }}>
      {children}
    </AuthCtx.Provider>
  );
};

export const useAuth = () => useContext(AuthCtx);