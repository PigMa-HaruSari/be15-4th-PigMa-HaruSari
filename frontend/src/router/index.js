import { createRouter, createWebHistory } from 'vue-router';
import { statisticsRoutes } from '@/features/statistics/router.js';
import { mainRoutes } from '@/features/main/router.js';
import {loginRoutes} from "@/features/user/router.js";
import {useUserStore} from "@/stores/userStore.js";
import {categoryRoutes} from "@/features/category/router.js";

const router = createRouter({
  history: createWebHistory(),
  routes: [...statisticsRoutes, ...mainRoutes, ...loginRoutes, ...categoryRoutes],
});

router.beforeEach((to) => {
  const userStore = useUserStore();
  const isLoggedIn = !!userStore.userId;

  const publicPages = ['/login'];
  const authRequired = !publicPages.includes(to.path);

  if (authRequired && !isLoggedIn) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  if (to.name === 'login' && isLoggedIn) {
    return { name: 'main' };
  }
});


export default router;
