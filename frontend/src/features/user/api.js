import api from '@/lib/axios';

export function loginUser(data) {
  return api.post('/auth/login', data);
}

export function sendEmailCode(email) {
  console.log("*** sendEmailCode 실행 ***")
  console.log(email)
  return api.post('/auth/email/send', { email });
}

export function verifyEmailCode(email, code) {
  return api.post('/auth/email/verify', {
    email, code
  });
}

export function registerUser(payload) {
  return api.post('/auth/signup', payload);
}