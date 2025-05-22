import api from '@/lib/axios';

export const loginUser = (data) => api.post('/auth/login', data);