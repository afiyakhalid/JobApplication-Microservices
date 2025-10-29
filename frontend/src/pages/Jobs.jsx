import React, { useEffect, useState } from 'react';
import axios from '../api/axios';

export default function Jobs() {
  const empty = { title: '', description: '', location: '', minSalary: '', maxSalary: '', companyId: '' };
  const [jobs, setJobs] = useState([]);
  const [form, setForm] = useState(empty);
  const [companiesMap, setCompaniesMap] = useState({});

  const fetch = async () => {
    try {
      const { data } = await axios.get('/jobs');
      setJobs(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error('Fetch jobs error', err);
      setJobs([]);
    }
  };

  const fetchCompanies = async () => {
    try {
      const { data } = await axios.get('/companies');
      const map = {};
      (Array.isArray(data) ? data : []).forEach((c) => (map[c.id] = c));
      setCompaniesMap(map);
    } catch (err) {
      console.error('Fetch companies error', err);
      setCompaniesMap({});
    }
  };

  useEffect(() => {
    fetchCompanies();
    fetch();
  }, []);

  const save = async () => {
    // simple validation
    if (!form.title || !form.companyId) {
      alert('Please provide at least title and companyId (or choose a company).');
      return;
    }

    // prepare payload (ensure companyId type is what backend expects)
    const payload = {
      ...form,
      // attempt to convert numeric-looking ids to numbers
      companyId: typeof form.companyId === 'string' && /^\d+$/.test(form.companyId) ? Number(form.companyId) : form.companyId,
    };

    try {
      if (form.id) {
        await axios.put(`/jobs/${form.id}`, payload);
      } else {
        await axios.post('/jobs', payload);
      }
      setForm(empty);
      await fetch();
    } catch (err) {
      console.error('Save job error', err);
      alert(err?.response?.data?.message || 'Failed to save job. See console for details.');
    }
  };

  const remove = async (id) => {
    if (!confirm('Delete this job?')) return;
    try {
      await axios.delete(`/jobs/${id}`);
      fetch();
    } catch (err) {
      console.error('Delete job error', err);
      alert('Failed to delete job.');
    }
  };

  const edit = (job) => {
    setForm({
      id: job.id,
      title: job.title || '',
      description: job.description || '',
      location: job.location || '',
      minSalary: job.minSalary || '',
      maxSalary: job.maxSalary || '',
      companyId: job.companyId || (job.company && job.company.id) || '',
    });
    // scroll or focus could be added here if you want UX improvements
  };

  return (
    <div>
      <h3>Jobs</h3>

      <table className="table">
        <thead>
          <tr>
            <th>Title</th><th>Company</th><th>Location</th><th>Salary</th><th></th>
          </tr>
        </thead>
        <tbody>
          {jobs.map((j) => (
            <tr key={j.id}>
              <td>{j.title}</td>
              <td>{(j.company && j.company.name) || companiesMap[j.companyId]?.name || '-'}</td>
              <td>{j.location || '-'}</td>
              <td>{j.minSalary ? `${j.minSalary} - ${j.maxSalary || ''}` : '-'}</td>
              <td>
                <button className="btn" onClick={() => edit(j)}>Edit</button>{' '}
                <button className="btn danger" onClick={() => remove(j.id)}>Delete</button>
              </td>
            </tr>
          ))}
          {jobs.length === 0 && <tr><td colSpan={5} className="muted">No jobs yet</td></tr>}
        </tbody>
      </table>

      <hr />
      <h4>{form.id ? 'Edit Job' : 'Add Job'}</h4>
      <div className="form-row">
        <input placeholder="title" value={form.title || ''} onChange={(e) => setForm({ ...form, title: e.target.value })} />
        <input placeholder="location" value={form.location || ''} onChange={(e) => setForm({ ...form, location: e.target.value })} />
        <input placeholder="min salary" value={form.minSalary || ''} onChange={(e) => setForm({ ...form, minSalary: e.target.value })} />
        <input placeholder="max salary" value={form.maxSalary || ''} onChange={(e) => setForm({ ...form, maxSalary: e.target.value })} />
        <input placeholder="companyId" value={form.companyId || ''} onChange={(e) => setForm({ ...form, companyId: e.target.value })} />
      </div>
      <div style={{ marginTop: 8 }}>
        <button className="btn primary" onClick={save}>{form.id ? 'Update' : 'Create'}</button>{' '}
        <button className="btn" onClick={() => setForm(empty)}>Reset</button>
      </div>
    </div>
  );
}