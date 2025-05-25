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

export function fetchMyPageProfile() {
  return api.get('/users/mypage');
}

export function updateUserProfile(data) {
  return api.put('/users/mypage', data);
}

export function updateUserPassword(data) {
  return api.put('/users/password', data);
}

export function signOutUser(data) {
  return api.put('/users/signout', data);
}

export function sendResetEmailCode(email) {
  return api.post('/users/reset-password/request', {email});
}

export function verifyResetToken(email, token) {
  return api.post('/users/reset-password/verify', { email, token });
}

export function resetPasswordRequest(payload) {
  return api.post('/users/reset-password', payload);
}
