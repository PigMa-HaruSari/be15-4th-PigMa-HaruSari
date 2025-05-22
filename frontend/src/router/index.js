import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from "@/stores/userStore.js";

import { statisticsRoutes } from '@/features/statistics/router.js';
import { mainRoutes } from '@/features/main/router.js';
import { userRoutes } from "@/features/user/router.js";
import { categoryRoutes } from "@/features/category/router.js";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...statisticsRoutes,
    ...mainRoutes,
    ...userRoutes,
    ...categoryRoutes
  ],
});

// 비로그인 상태로 접근 시 차단하는 설정
router.beforeEach((to) => {
  const userStore = useUserStore();
  const isLoggedIn = userStore.isAuthenticated;

  // 회원만 접속 가능한 페이지이나 비로그인 상태 : 로그인 페이지로 이동
  if (to.meta.requiresAuth && !isLoggedIn) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  // 이미 로그인 된 상황에서 로그인 페이지에 접근하면 메인으로 이동
  if (to.meta.requiresGuest && isLoggedIn) {
    return { name: 'main' };
  }
});

export default router;
