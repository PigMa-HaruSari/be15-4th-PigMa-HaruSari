import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import { jwtDecode } from 'jwt-decode'

import App from './App.vue'
import router from './router'
import { useUserStore } from '@/stores/userStore.js'
import { refreshUserToken } from '@/lib/user.js'
import { closeSSE, connectSSE } from '@/utill/connectSSE.js';

window.addEventListener('beforeunload', () => {
  closeSSE();
});

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

// 사용자 정보 복원 및 토큰 재발급
const userStore = useUserStore()
const savedUser = localStorage.getItem('user')

if (savedUser) {
  const parsedUser = JSON.parse(savedUser)
  userStore.setUser(parsedUser)

  refreshUserToken()
    .then(res => {
      const newToken = res.data.data.accessToken
      const payload = jwtDecode(newToken)

      userStore.setUser({
        ...parsedUser,
        accessToken: newToken
      })

      if (userStore.isAuthenticated) {
        connectSSE();
      }
    })
    .catch(() => {
      userStore.logout();
    });
}

// 앱 마운트
app.mount('#app')
