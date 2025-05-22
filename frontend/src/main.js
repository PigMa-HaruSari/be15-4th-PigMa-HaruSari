import {createApp} from 'vue'
import {createPinia} from 'pinia'
import '/src/assets/css/header.css'
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';


import App from './App.vue'
import router from './router'

import {
    Chart,
    ArcElement,
    BarElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
    DoughnutController,
    BarController
} from 'chart.js'
import {useUserStore} from "@/stores/userStore.js";

Chart.register(
    ArcElement,
    BarElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
    DoughnutController,
    BarController
)

const app = createApp(App)

app.use(createPinia())
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
});

const userStore = useUserStore();
const savedUser = localStorage.getItem('user');
if (savedUser) {
    userStore.setUser(JSON.parse(savedUser));
}



app.mount('#app')
