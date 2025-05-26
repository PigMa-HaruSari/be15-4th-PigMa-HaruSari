import { EventSourcePolyfill } from 'event-source-polyfill';
import { emitter } from '@/utill/emitter.js'; // mitt 기반 이벤트 버스
import api from '@/lib/axios.js';
import { useUserStore } from '@/stores/userStore.js'; // Optional: 테스트용 API 호출

let sse = null;

/**
 * SSE 연결 함수
 */
export const connectSSE = () => {
    const userStore = useUserStore();

    if (!userStore.isLoggedIn) {
        console.warn('로그인 상태 아님 - SSE 연결 중단');
        return;
    }

    if (window.__sse__) {
        console.log('SSE 이미 연결됨, 중단');
        return;
    }

    const token = localStorage.getItem('accessToken');
    if (!token || token.trim() === '') {
        console.warn('accessToken 없음 또는 공백 - SSE 연결 중단');
        return;
    }

    sse = new EventSourcePolyfill(`${import.meta.env.VITE_API_BASE_URL}/alarm`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
        heartbeatTimeout: 600000,
    });

    window.__sse__ = sse;

    sse.onopen = () => console.log('✅ [SSE] 연결 성공');

    sse.onerror = (err) => {
        console.error('❌ [SSE] 연결 오류', err);
        emitter.emit('sse-error', err); // 필요시 로직에 따라 처리
    };

    sse.onmessage = (event) => {
        try {
            const parsed = JSON.parse(event.data);
            emitter.emit('notification', parsed);
        } catch {
            emitter.emit('notification', { message: event.data });
        }
    };

    sse.addEventListener('alarm', (event) => {
        try {
            const parsed = JSON.parse(event.data);
            emitter.emit('notification', parsed);
        } catch {
            emitter.emit('notification', { message: event.data });
        }
    });
};

/**
 * 수동으로 알림 API 요청 (SSE가 아닌 일반 GET 요청)
 */
export const MessageSse = () => {
    return api.get('/alarm');
};

/**
 * SSE 연결 종료 함수
 */
export const closeSSE = () => {
    if (window.__sse__) {
        window.__sse__.close();
        window.__sse__ = null;
        sse = null;
        console.log('🔌 [SSE] 연결 종료');
    }
};
