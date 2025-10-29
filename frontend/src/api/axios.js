import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8085', // gateway
  headers: {
    'Content-Type': 'application/json',
  },
});

instance.interceptors.request.use((cfg) => {
  const token = localStorage.getItem('token');
  if (token) cfg.headers.Authorization = `Bearer ${token}`;
  return cfg;
});

export default instance;