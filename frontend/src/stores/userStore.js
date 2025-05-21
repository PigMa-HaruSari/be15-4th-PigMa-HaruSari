import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
/*    state: () => ({
        userId: null,
        email: '',
        nickname: ''
    }),
    actions: {
        setUser(userData) {
            this.userId = userData.userId;
            this.email = userData.email;
            this.nickname = userData.nickname;
        },
        logout() {
            this.userId = null;
            this.email = '';
            this.nickname = '';
        }
    }*/
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
            localStorage.setItem('user', JSON.stringify(userData));
        },
        logout() {
            this.userId = null;
            this.email = '';
            this.nickname = '';
            localStorage.removeItem('user');
        }
    }
});
