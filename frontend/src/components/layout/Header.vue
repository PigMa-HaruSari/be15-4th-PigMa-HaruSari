<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
        HARUSARI
      </router-link>
      <nav class="nav">
        <router-link to="/category">
          <img src="@/assets/images/header-bell-white.svg" alt="알림" />
        </router-link>
        <router-link to="/category">
          <img src="@/assets/images/header-categories-white.svg" alt="카테고리" />
        </router-link>
        <router-link to="/statistics">
          <img src="@/assets/images/header-statistics-white.svg" alt="통계" />
        </router-link>
        <router-link to="/mypage">
          <img src="@/assets/images/header-profile-white.svg" alt="마이페이지" />
        </router-link>
        <button @click="logoutAndRedirect">로그아웃</button>
      </nav>

    </div>
  </header>
</template>

<script setup>

import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const logoutAndRedirect = () => {
  userStore.logout();
  router.push('/login');
}
</script>

<style scoped>


.header {
  width: 100vw;
  height: 100px;
  background-color: #b8b8ff;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-inner {
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0; /* padding 제거 */
}
.logo {
  font-size: 30px;
  font-weight: bold;
  color: #ffffff;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 10px;
}
.logo img {
  height: 60px;
}
.nav {
  display: flex;
  gap: 20px;
}
.nav button {
  background: none;
  border: none;
  font-size: 16px;
  color: #ffffff;
  cursor: pointer;
}
</style>
