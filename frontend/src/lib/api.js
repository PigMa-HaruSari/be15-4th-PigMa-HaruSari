import axios from 'axios'
import { useUserStore } from '@/stores/userStore';
import router from '@/router';
import { showErrorToast, showSuccessToast } from '@/utils/toast';



const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    withCredentials: true // 필요한 경우만
});
// 요청 인터셉터: accessToken 자동 삽입
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// 응답 인터셉터: 메시지 + 오류 대응 + 토큰 만료 감지
api.interceptors.response.use(
    (response) => {
        // ✅ 성공 메시지 출력 (GET은 제외)
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

        // 🔐 리프레시 요청 자체가 실패한 경우 → 로그아웃
        if (config.url.includes('/auth/refresh')) {
            userStore.logout();
            localStorage.removeItem('accessToken');
            showErrorToast('세션이 만료되었습니다. 다시 로그인해주세요.');
            return Promise.reject(error);
        }

        // 🔒 인증 실패
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
                    const refreshRes = await axios.post(
                        'http://localhost:8080/api/v1/auth/refresh',
                        {},
                        { withCredentials: true }
                    );
                    const newToken = refreshRes.data.data.accessToken;

                    // ✅ 새로운 accessToken 저장
                    localStorage.setItem('accessToken', newToken);

                    // ✅ 요청에 새로운 토큰 삽입 후 재시도
                    config.headers.Authorization = `Bearer ${newToken}`;
                    return api(config);
                } catch (refreshErr) {
                    userStore.logout();
                    localStorage.removeItem('accessToken');
                    showErrorToast('세션이 만료되었습니다. 다시 로그인해주세요.');
                    return Promise.reject(refreshErr);
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



export default api
