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
        <div class="profile-dropdown">
          <img src="@/assets/images/header-profile-white.svg" alt="마이페이지"
               @click="toggleDropdown" class="profile-icon" />
          <div v-if="showDropdown" class="dropdown-menu">
            <router-link to="/mypage">마이페이지</router-link>
            <router-link to="/feedback">피드백</router-link>
            <button @click="logout">로그아웃</button>
          </div>
        </div>
      </nav>

    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

const showDropdown = ref(false);
const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

// 드롭다운 닫을 때 외부 클릭해도 닫히는 설정
onMounted(() => {
  const handleClickOutside = (e) => {
    if (!e.target.closest('.profile-dropdown')) {
      showDropdown.value = false;
    }
  };
  window.addEventListener('click', handleClickOutside);

  onBeforeUnmount(() => {
    window.removeEventListener('click', handleClickOutside);
  });
});

const userStore = useUserStore();
const router = useRouter();

const logout = () => {
  userStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.header {
  width: 100%;
  height: 100px;
  background-color: #b8b8ff;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;        /* 👉 항상 상단 고정 */
  top: 0;
  left: 0;
  z-index: 100;
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
.profile-dropdown {
  position: relative;
}

.profile-icon {
  cursor: pointer;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  transition: transform 0.2s ease;
}
.profile-icon:hover {
  transform: scale(1.05);
}

.dropdown-menu {
  position: absolute;
  top: 120%;
  right: 0;
  background-color: #fff;
  color: #333;
  width: 180px;
  padding: 0.75rem 0;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  box-sizing: border-box;  /* ✅ padding 포함한 총 너비 보장 */
  overflow: hidden;        /* ✅ 삐져나오는 것 잘라냄 */
  display: flex;
  flex-direction: column;
  font-family: 'Segoe UI', sans-serif;
  font-size: 0.95rem;
}

.dropdown-menu a,
.dropdown-menu button {
  padding: 0.65rem 1.2rem;
  width: 100%;             /* ✅ 부모 너비에 딱 맞춤 */
  text-align: left;
  background: none;
  border: none;
  cursor: pointer;
  color: inherit;
  overflow: hidden;        /* ✅ hover 시 배경 넘침 방지 */
  transition: background 0.2s;
}

.dropdown-menu a:hover,
.dropdown-menu button:hover {
  background-color: #f0f0ff;
  color: #5e4bff;
  font-weight: 500;
  border-left: 4px solid #9381FF;
}
</style>
