import { EventSourcePolyfill } from 'event-source-polyfill';
import { emitter } from '@/utill/emitter.js'; // mitt 기반 이벤트 버스
import api from '@/lib/axios.js'; // Optional: 테스트용 API 호출

let sse = null;

/**
 * SSE 연결 함수
 */
export const connectSSE = () => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
        console.warn('❌ accessToken 없음 - SSE 연결 중단');
        return;
    }

    sse = new EventSourcePolyfill(`${import.meta.env.VITE_API_BASE_URL}/alarm`, {
        headers: {
            Authorization: `Bearer ${token}`
        },
        heartbeatTimeout: 600000,
    });

    sse.onopen = () => {
        console.log('✅ [SSE] 연결 성공');
    };

    sse.onerror = (err) => {
        console.error('❌ [SSE] 연결 오류', err);
    };

    // 기본 메시지 수신 (event name 생략된 경우)
    sse.onmessage = (event) => {
        console.log('📥 [SSE] 기본 메시지 수신:', event.data);
        try {
            const parsed = JSON.parse(event.data);
            emitter.emit('notification', parsed);
        } catch (err) {
            emitter.emit('notification', { message: event.data });
        }
    };

    // 커스텀 이벤트 "alarm" 수신
    sse.addEventListener('alarm', (event) => {
        console.log('📡 [SSE] alarm 이벤트 수신:', event.data);
        try {
            const parsed = JSON.parse(event.data);
            emitter.emit('notification', parsed);
        } catch (err) {
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
    if (sse) {
        sse.close();
        sse = null;
        console.log('🔌 [SSE] 연결 종료');
    }
};
