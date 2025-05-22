import { defineStore } from 'pinia';

/* Pinia 사용자 정보 저장소 */
let logoutTimer = null; // ⏱️ 타이머 전역 변수

export const useUserStore = defineStore('user', {
    state: () => ({
        userId: null,
        email: '',
        nickname: ''
    }),
    actions: {
        setUser(userData) {
            this.userId = userData.userId;
            this.email = userData.email;
            this.nickname = userData.nickname;

            // ✅ localStorage 저장
            localStorage.setItem('user', JSON.stringify(userData));
            localStorage.setItem('accessToken', userData.accessToken);

        },
        logout() {
            this.userId = null;
            this.email = '';
            this.nickname = '';

            localStorage.removeItem('user');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');


            clearTimeout(logoutTimer);
        }

    }
});