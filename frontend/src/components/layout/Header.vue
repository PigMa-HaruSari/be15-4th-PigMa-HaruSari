<!-- Header.vue -->
<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import AlarmDropdown from '@/features/alarm/components/AlarmDropdown.vue'

const isDropdownOpen = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const closeDropdown = () => {
  isDropdownOpen.value = false
}

const handleClickOutside = (event) => {
  const bell = document.querySelector('img[alt="알림"]')
  const dropdown = document.querySelector('.notification-modal')
  if (
      dropdown &&
      !dropdown.contains(event.target) &&
      bell &&
      !bell.contains(event.target)
  ) {
    closeDropdown()
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <header>
    <div class="header-inner">
      <div class="logo">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
        HARUSARI
      </div>
      <nav>
        <div class="bell-wrapper">
          <img
              src="@/assets/images/header-bell-white.svg"
              alt="알림"
              @click="toggleDropdown"
          />
          <AlarmDropdown
              v-if="isDropdownOpen"
              @close="closeDropdown"
          />
        </div>

        <img src="@/assets/images/header-statistics-white.svg" alt="통계" />
        <img src="@/assets/images/header-categories-white.svg" alt="카테고리" />
        <img src="@/assets/images/header-profile-white.svg" alt="마이페이지" />
      </nav>
    </div>
  </header>
</template>

<style scoped>
header {
  width: 100%;
  background: var(--header, #b8b8ff);
  height: 100px;
  display: flex;
  align-items: center;
}

.header-inner {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 30px;
  font-weight: bold;
  color: #ffffff;
  display: flex;
  align-items: center;
}

.logo img {
  height: 60px;
  margin-right: 10px;
}

nav {
  display: flex;
  gap: 20px;
  align-items: center;
}

nav img {
  cursor: pointer;
}

.bell-wrapper {
  position: relative;
}
</style>
