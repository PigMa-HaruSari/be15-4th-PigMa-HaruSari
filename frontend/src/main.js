import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import { jwtDecode } from 'jwt-decode'

import App from './App.vue'
import router from './router'
import { useUserStore } from '@/stores/userStore.js'
import { refreshUserToken } from '@/lib/user.js'

// 앱 생성 및 플러그인 연결
const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(Toast, {
    position: 'top-right',
    timeout: 3000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
    hideProgressBar: false,
    closeButton: 'button',
    icon: true,
    rtl: false,
})

// localStorage 초기화
const userStore = useUserStore()
const savedUser = localStorage.getItem('user')

if (savedUser) {
  const parsedUser = JSON.parse(savedUser);
  userStore.setUser(parsedUser);

  // accessToken 재발급 시도는 로그인 상태일 때만
  refreshUserToken()
    .then(res => {
      const newToken = res.data.data.accessToken;
      const payload = jwtDecode(newToken);
      userStore.setUser({
        accessToken: newToken,
        userId: payload.sub,
        nickname: payload.nickname,
        role: payload.role,
        expiration: payload.exp * 1000
      });
    })
    .catch(() => {
      userStore.logout();
    });
}

// 새로고침 시 refreshToken으로 accessToken 재발급 시도
refreshUserToken()
  .then(res => {
      const newToken = res.data.data.accessToken
      const payload = jwtDecode(newToken)

      userStore.setUser({
          accessToken: newToken,
          userId: payload.sub,
          nickname: payload.nickname, // 사용 시
          role: payload.role,
          expiration: payload.exp * 1000
      })
  })
  .catch(() => {
      userStore.logout()
  })

// 앱 마운트
app.mount('#app')
