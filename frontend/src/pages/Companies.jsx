import React, { useEffect, useState } from 'react';
import axios from '../api/axios';

export default function Companies() {
  const empty = { name: '', description: '' };
  const [list, setList] = useState([]);
  const [form, setForm] = useState(empty);

  const fetch = async () => {
    try {
      const { data } = await axios.get('/companies');
      setList(Array.isArray(data) ? data : []);
    } catch (err) {
      setList([]);
    }
  };

  useEffect(() => { fetch(); }, []);

  const save = async () => {
    try {
      if (form.id) {
        await axios.put(`/companies/${form.id}`, form);
      } else {
        await axios.post('/companies', form);
      }
      setForm(empty);
      fetch();
    } catch (err) {
      console.error('Save company error', err);
      alert('Failed to save company');
    }
  };

  const remove = async (id) => {
    if (!confirm('Delete this company? This may remove related jobs/reviews depending on backend.')) return;
    try {
      await axios.delete(`/companies/${id}`);
      fetch();
    } catch (err) {
      console.error('Delete company error', err);
      alert('Failed to delete');
    }
  };

  const edit = (c) => setForm({ id: c.id, name: c.name || '', description: c.description || '' });

  return (
    <div>
      <h3>Companies</h3>

      <table className="table">
        <thead>
          <tr><th>Name</th><th>Description</th><th /></tr>
        </thead>
        <tbody>
          {list.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>{c.description}</td>
              <td>
                <button className="btn" onClick={() => edit(c)}>Edit</button>{' '}
                <button className="btn danger" onClick={() => remove(c.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <hr />
      <h4>{form.id ? 'Edit Company' : 'Add Company'}</h4>
      <div className="form-row">
        <input placeholder="name" value={form.name || ''} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input placeholder="description" value={form.description || ''} onChange={(e) => setForm({ ...form, description: e.target.value })} />
      </div>
      <div style={{ marginTop: 8 }}>
        <button className="btn primary" onClick={save}>{form.id ? 'Update' : 'Create'}</button>{' '}
        <button className="btn" onClick={() => setForm(empty)}>Reset</button>
      </div>
    </div>
  );
}