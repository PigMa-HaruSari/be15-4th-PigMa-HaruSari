/* Pinia 설정 */
import { defineStore } from 'pinia';
import api from '@/lib/axios.js';

// 자동 로그아웃 기능을 위한 타이머 변수
let logoutTimer = null;

/* 'user'라는 이름의 Store 정의 */
export const useUserStore = defineStore('user', {
    state: () => ({
        userId: null,
        email: '',
        nickname: '',
        accessToken: null,
        expiration: null
    }),

    // 로그인 여부 검증을 위한 로직 추가
    getters: {
        isAuthenticated(state) {
            return !!state.accessToken && Date.now() < (state.expiration || 0);
        }
    },

    actions: {
        setUser(userData) {
            this.userId = userData.userId;
            this.email = userData.email;
            this.nickname = userData.nickname;
            this.accessToken = userData.accessToken;
            this.expiration = userData.expiration;

            localStorage.setItem('user', JSON.stringify(userData));
            localStorage.setItem('accessToken', userData.accessToken);
        },

        logout() {
            api.post('/auth/logout').catch(() => {});

            this.userId = null;
            this.email = '';
            this.nickname = '';
            this.accessToken = null;
            this.expiration = null;

            localStorage.removeItem('user');
            localStorage.removeItem('accessToken');
        }
    }
});