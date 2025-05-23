import { nextTick } from 'vue';
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

router.beforeEach(async (to, from) => {
  const userStore = useUserStore();

  // 아직 초기화되지 않았는데 라우터가 먼저 작동한 경우를 대비
  if (!userStore.accessToken) {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      userStore.setUser(JSON.parse(savedUser));
      await nextTick(); // 상태 반영 대기
    }
  }

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
