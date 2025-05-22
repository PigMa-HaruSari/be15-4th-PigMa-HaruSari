import api from './axios.js';

export function refreshUserToken() {
  return api.post('/auth/refresh');  // 쿠키 기반 요청이므로 별도 헤더/바디 없이 호출
}