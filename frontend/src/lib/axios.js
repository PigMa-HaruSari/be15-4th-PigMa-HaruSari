import axios from 'axios';
import { useUserStore } from '@/stores/userStore';
import { showErrorToast, showSuccessToast } from '@/utill/toast.js';
import {refreshUserToken} from "@/lib/user.js";

/* Axios 인스턴스 생성 */
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    withCredentials: true,
});

/* 토큰 재발급 중복 요청 방지 플래그와 대기 큐 */
let isRefreshing = false;
let refreshSubscribers = [];

/* 요청에 대해 새 토큰 전달 후 알림 */
const subscribeTokenRefresh = (cb) => {
    refreshSubscribers.push(cb);
};

const onTokenRefreshed = (token) => {
    refreshSubscribers.forEach((cb) => cb(token));
    refreshSubscribers = [];
};

/* 요청 인터셉터: accessToken을 Authorization 헤더에 자동 삽입*/
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    // console.log('[요청 인터셉터] accessToken:', token); // ✅ 추가

    // 비로그인 상태에서 수행하는 로직인 경우는 삽입 X
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    } else {
        delete config.headers.Authorization;
    }

    return config;
});

/* 응답 인터셉터 */
api.interceptors.response.use(
    (response) => {
        /* 1. 성공 알림 토스트 출력(GET이 아닐 때) */
        if (response.config.method !== 'get') {
            // showSuccessToast(response.data.message);
        }
        return response;
    },
    /* 2. 에러에 대한 처리 */
    async (error) => {
        const { config, response } = error;
        const userStore = useUserStore();

        // 1) 응답이 없을 때
        if (!response) {
            showErrorToast('서버와 연결할 수 없습니다. 인터넷 상태를 확인해주세요.');
            return Promise.reject(error);
        }

        const status = response.status;
        const errorCode = response.data?.errorCode;
        const message = response.data?.message || '알 수 없는 오류가 발생했습니다.';

        // 2) 401 Unauthorized 처리
        if (status === 401) {
            // 이미 재시도 했으면 로그아웃 처리
            if (config._retry) {
                userStore.logout();
                localStorage.removeItem('accessToken');
                showErrorToast('인증이 만료되었습니다. 다시 로그인해주세요.');
                return Promise.reject(error);
            }

            config._retry = true;

            // JWT 만료인 경우
            if (errorCode === 'EXPIRED_JWT') {
                try {
                    if (!isRefreshing) {
                        isRefreshing = true;
                        const refreshRes = await refreshUserToken();  // Using the refreshUserToken function
                        const newToken = refreshRes.data.data.accessToken;

                        localStorage.setItem('accessToken', newToken);
                        api.defaults.headers['Authorization'] = `Bearer ${newToken}`;
                        onTokenRefreshed(newToken);
                    } else {
                        return new Promise((resolve) => {
                            subscribeTokenRefresh((token) => {
                                config.headers['Authorization'] = 'Bearer ' + token;
                                resolve(axios(config));
                            });
                        });
                    }

                    return axios(config);
                } catch (refreshErr) {
                    userStore.logout();
                    localStorage.removeItem('accessToken');
                    showErrorToast('세션이 만료되었습니다. 다시 로그인해주세요.');
                    return Promise.reject(refreshErr);
                } finally {
                    isRefreshing = false;
                }
            } else {
                // 기타 401 에러에 대해 로그아웃 처리
                userStore.logout();
                localStorage.removeItem('accessToken');
                showErrorToast(message);
                return Promise.reject(error);
            }
        }

        // 3) 기타 에러 처리
        if (status === 403) {
            showErrorToast(message || '접근 권한이 없습니다.');
        } else if (status >= 400 && status < 500) {
            showErrorToast(message);
        } else if (status >= 500) {
            showErrorToast(message || '서버 오류가 발생했습니다.');
        }

        return Promise.reject(error);
    }
);

export default api;
