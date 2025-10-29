import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const [u, setU] = useState('');
  const [p, setP] = useState('');
  const [error, setError] = useState('');

  const handle = async (e) => {
    e.preventDefault();
    setError('');
    const res = await login(u.trim(), p);
    if (res.ok) {
      // redirect to dashboard after successful login
      navigate('/dashboard', { replace: true });
    } else {
      setError(res.message || 'Invalid credentials');
    }
  };

  return (
    <div className="center-screen card">
      <h2>Sign in</h2>
      <form onSubmit={handle} className="form">
        <input
          placeholder="username"
          value={u}
          onChange={(e) => setU(e.target.value)}
          required
          autoFocus
        />
        <input
          type="password"
          placeholder="password"
          value={p}
          onChange={(e) => setP(e.target.value)}
          required
        />
        <button className="btn primary" type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Login'}
        </button>
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  );
}