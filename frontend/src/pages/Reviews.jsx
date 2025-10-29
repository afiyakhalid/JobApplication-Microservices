import React, { useEffect, useState } from 'react';
import axios from '../api/axios';

export default function Reviews() {
  const [reviews, setReviews] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [companyId, setCompanyId] = useState('');
  const [form, setForm] = useState({ title: '', description: '' });

  const fetchCompanies = async () => {
    try {
      const { data } = await axios.get('/companies');
      setCompanies(Array.isArray(data) ? data : []);
    } catch (err) {
      setCompanies([]);
    }
  };

  const fetchReviews = async (cid) => {
    if (!cid) {
      setReviews([]);
      return;
    }
    try {
      const { data } = await axios.get('/reviews', { params: { companyId: cid } });
      setReviews(Array.isArray(data) ? data : []);
    } catch (err) {
      setReviews([]);
    }
  };

  useEffect(() => { fetchCompanies(); }, []);
  useEffect(() => { if (companyId) fetchReviews(companyId); else setReviews([]); }, [companyId]);

  const save = async () => {
    try {
      await axios.post('/reviews', { ...form, companyId });
      setForm({ title: '', description: '' });
      fetchReviews(companyId);
    } catch (err) {
      console.error('Save review error', err);
      alert('Failed to save review');
    }
  };

  const remove = async (id) => {
    if (!confirm('Delete this review?')) return;
    try {
      await axios.delete(`/reviews/${id}`);
      fetchReviews(companyId);
    } catch (err) {
      console.error('Delete review error', err);
      alert('Failed to delete review');
    }
  };

  return (
    <div>
      <h3>Reviews</h3>

      <div className="form-row">
        <label>
          Company:
          <select value={companyId} onChange={(e) => setCompanyId(e.target.value)}>
            <option value="">-- choose company --</option>
            {companies.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </label>
      </div>

      <table className="table">
        <thead>
          <tr><th>Title</th><th>Description</th><th /></tr>
        </thead>
        <tbody>
          {reviews.map((r) => (
            <tr key={r.id}>
              <td>{r.title}</td>
              <td>{r.description}</td>
              <td>
                <button className="btn danger" onClick={() => remove(r.id)}>Del</button>
              </td>
            </tr>
          ))}
          {reviews.length === 0 && (
            <tr><td colSpan={3} className="muted">No reviews for selected company</td></tr>
          )}
        </tbody>
      </table>

      <hr />
      <h4>Add Review</h4>
      <div className="form-row">
        <input placeholder="title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
        <input placeholder="description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
      </div>
      <div style={{ marginTop: 8 }}>
        <button className="btn primary" disabled={!companyId} onClick={save}>Save</button>
      </div>
    </div>
  );
}