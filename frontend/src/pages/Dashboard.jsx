import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Jobs from './Jobs';
import Companies from './Companies';
import Reviews from './Reviews';

export default function Dashboard() {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
  const [tab, setTab] = useState('jobs');

  const doLogout = () => {
    logout();
    // navigate to login immediately after logout
    navigate('/login', { replace: true });
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="brand">Jobs & Companies Admin</div>
        <div className="user-actions">
          <span className="muted">Signed in as {user}</span>
          <button className="btn" onClick={doLogout}>Logout</button>
        </div>
      </header>

      <main className="main">
        <aside className="sidebar">
          <button className={tab === 'jobs' ? 'btn primary full' : 'btn full'} onClick={() => setTab('jobs')}>Jobs</button>
          <button className={tab === 'companies' ? 'btn primary full' : 'btn full'} onClick={() => setTab('companies')}>Companies</button>
          <button className={tab === 'reviews' ? 'btn primary full' : 'btn full'} onClick={() => setTab('reviews')}>Reviews</button>
        </aside>

        <section className="content">
          {tab === 'jobs' && <Jobs />}
          {tab === 'companies' && <Companies />}
          {tab === 'reviews' && <Reviews />}
        </section>
      </main>
    </div>
  );
}