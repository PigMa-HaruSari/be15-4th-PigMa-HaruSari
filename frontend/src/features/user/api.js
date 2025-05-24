import api from '@/lib/axios';

export function loginUser(data) {
  return api.post('/auth/login', data);
}

export function sendEmailCode(email) {
  console.log(email)
  return api.post('/auth/email/send', { email });
}

export function verifyEmailCode(email, code) {
  return api.post('/auth/email/verify', {
    email, code
  });
}

export function getKakaoUserInfo(code) {
  return api.get(`/auth/social/info/kakao?code=${code}`);
}

export function registerUser(payload) {
  return api.post('/auth/signup', payload);
}

export function registerKakaoUser(payload) {
  return api.post('/auth/social/signup/kakao', payload);
}
