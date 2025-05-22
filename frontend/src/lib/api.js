import axios from 'axios';
import { useUserStore } from '@/stores/userStore';
import { showErrorToast, showSuccessToast } from '@/utill/toast.js';
import {refreshUserToken} from "@/lib/user.js";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    withCredentials: true,
});

let isRefreshing = false;
let refreshSubscribers = [];

const subscribeTokenRefresh = (cb) => {
    refreshSubscribers.push(cb);
};

const onTokenRefreshed = (token) => {
    refreshSubscribers.forEach((cb) => cb(token));
    refreshSubscribers = [];
};

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    console.log('[요청 인터셉터] accessToken:', token); // ✅ 추가

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => {
        if (response.config.method !== 'get') {
            showSuccessToast(response.data.message);
        }
        return response;
    },
    async (error) => {
        const { config, response } = error;
        const userStore = useUserStore();

        if (!response) {
            showErrorToast('서버와 연결할 수 없습니다. 인터넷 상태를 확인해주세요.');
            return Promise.reject(error);
        }

        const status = response.status;
        const errorCode = response.data?.errorCode;
        const message = response.data?.message || '알 수 없는 오류가 발생했습니다.';

        if (status === 401) {
            if (config._retry) {
                userStore.logout();
                localStorage.removeItem('accessToken');
                showErrorToast('인증이 만료되었습니다. 다시 로그인해주세요.');
                return Promise.reject(error);
            }

            config._retry = true;

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
                userStore.logout();
                localStorage.removeItem('accessToken');
                showErrorToast(message);
                return Promise.reject(error);
            }
        }

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
