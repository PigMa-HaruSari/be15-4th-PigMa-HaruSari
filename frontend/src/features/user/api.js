import api from '@/lib/axios';

export function loginUser(data) {
  return api.post('/auth/login', data);
}

export function sendEmailCode(email) {
  console.log("*** sendEmailCode 실행 ***")
  console.log(email)
  return api.post('/api/v1/auth/email/send', { email });
}

export function verifyEmailCode(email, code) {
  return api.post('/api/v1/auth/email/verify', {
    email, code
  });
}

export function registerUser(payload) {
  return api.post('/api/v1/auth/signup', payload);
}