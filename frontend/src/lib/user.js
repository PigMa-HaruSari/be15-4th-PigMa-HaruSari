import api from './axios.js';
import { showErrorToast } from '@/utill/toast.js';

export function refreshUserToken() {
  const refreshToken = localStorage.getItem('refreshToken');

  // 리프레시 토큰 보유 여부에 따른 에러 추가
  if (!refreshToken) {
    showErrorToast('리프레시 토큰이 없습니다. 다시 로그인해주세요.');
    return Promise.reject('No refresh token found');
  }

  return api.post(
    '/auth/refresh',
    { refreshToken },
    {
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    }
  );
}