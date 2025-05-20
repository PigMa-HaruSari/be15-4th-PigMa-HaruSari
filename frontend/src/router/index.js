import {createRouter, createWebHistory} from 'vue-router'
import {statisticsRoutes} from "@/features/statistics/router.js";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        ...statisticsRoutes
    ],
})

export default router
