import {createApp} from 'vue'
import {createPinia} from 'pinia'

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

app.mount('#app')
