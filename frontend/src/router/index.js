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
  const isLoggedIn = !!userStore.userId;

  // 비회원도 접속 가능한 화면
  const publicPages = ['/login', '/signup', '/reset-password'];
  const authRequired = !publicPages.includes(to.path);

  // 회원만 접속 가능한 페이지이나 비로그인 상태 : 로그인 페이지로 이동
  if (authRequired && !isLoggedIn) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  // 이미 로그인 된 상황에서 로그인 페이지에 접근하면 메인으로 이동
  if (to.name === 'login' && isLoggedIn) {
    return { name: 'main' };
  }
});

export default router;
