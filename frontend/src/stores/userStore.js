/* Pinia 설정 */
import { defineStore } from 'pinia';

// 자동 로그아웃 기능을 위한 타이머 변수
let logoutTimer = null;

/* 'user'라는 이름의 Store 정의 */
export const useUserStore = defineStore('user', {
    // 로그인 한 유저 정보
    state: () => ({
        userId: null,
        email: '',
        nickname: ''
    }),

    // 상태 변경 메소드 (로그인 성공, 로그아웃)
    actions: {
        // 로그인 성공 시 사용자 정보를 localStorage에 저장
        setUser(userData) {
            this.userId = userData.userId;
            this.email = userData.email;
            this.nickname = userData.nickname;

            localStorage.setItem('user', JSON.stringify(userData));
            localStorage.setItem('accessToken', userData.accessToken);
        },
        // 로그아웃 시 localStorage에서 사용자 정보를 삭제
        logout() {
            this.userId = null;
            this.email = '';
            this.nickname = '';

            localStorage.removeItem('user');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');

            clearTimeout(logoutTimer); // TODO : 타이머 추가하기
        }
    }
});