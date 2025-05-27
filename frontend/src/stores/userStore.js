/* Pinia 설정 */
import { defineStore } from 'pinia';
import { jwtDecode } from 'jwt-decode';
import api from '@/lib/axios.js';
import { closeSSE } from '@/utill/connectSSE.js';

// 자동 로그아웃 기능을 위한 타이머 변수
let logoutTimer = null;

/* 'user'라는 이름의 Store 정의 */
export const useUserStore = defineStore('user', {
    state: () => ({
        userId: null,
        email: '',
        nickname: '',
        accessToken: null,
        expiration: null,
        userDeletedAt: false
    }),

    // 로그인 여부 검증을 위한 로직 추가
    getters: {
        isAuthenticated(state) {
            return !!state.accessToken && Date.now() < (state.expiration || 0) && !state.userDeletedAt;
        },
        isLoggedIn(state) {
            return !!state.accessToken && !state.userDeletedAt;
        }
    },

    actions: {
        setUser(userData) {
            // accessToken에서 exp 추출
            let expiration = userData.expiration ?? null

            if (!expiration && userData.accessToken) {
                try {
                    const decoded = jwtDecode(userData.accessToken)
                    expiration = decoded.exp * 1000
                    console.log('🕒 decoded.expiration:', expiration)
                } catch (e) {
                    console.error('❌ JWT 디코딩 실패:', e)
                }
            }
            this.userId = userData.userId;
            this.email = userData.email;
            this.nickname = userData.nickname;
            this.accessToken = userData.accessToken;
            this.expiration = expiration;
            this.userDeletedAt = userData.userDeletedAt || false;

            localStorage.setItem('user', JSON.stringify({
                userId: this.userId,
                email: this.email,
                nickname: this.nickname,
                accessToken: this.accessToken,
                expiration: this.expiration,
                userDeletedAt: this.userDeletedAt
            }))
            localStorage.setItem('accessToken', this.accessToken)
        },

        logout() {
            api.post('/auth/logout').catch(() => {});

            this.userId = null;
            this.email = '';
            this.nickname = '';
            this.accessToken = null;
            this.expiration = null;
            this.userDeletedAt = false;

            localStorage.removeItem('user');
            localStorage.removeItem('accessToken');
            closeSSE();
        }
    }
});