import {createRouter, createWebHistory} from 'vue-router'
import {statisticsRoutes} from "@/features/statistics/router.js";
import { easterEggRoutes } from "@/features/EasterEgg/router.js"

const router = createRouter({
  history: createWebHistory(),
  routes: [
      ...statisticsRoutes,
      ...easterEggRoutes
  ],
})

export default router
