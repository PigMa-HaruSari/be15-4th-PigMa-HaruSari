<template>
  <Header />
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="logo-box">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
      </div>
      <div class="subtitle">지금 하루살이에 가입하고 오늘의 삶을 기록해보세요</div>
      <button class="button">이메일 아이디로 가입하기</button>
      <button class="button">카카오로 가입하기</button>
    </div>
  </div>
</template>

<script setup>
import Header from '@/components/layout/Header.vue';

</script>

<style scoped>
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 75px);
  background: #f9f9f9;
}

.signup-box {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-box {
  margin-bottom: 16px;
}

.logo-box img {
  height: 80px;
}

.subtitle {
  margin-bottom: 24px;
  font-size: 16px;
  color: #555;
}

.button {
  width: 300px;
  padding: 14px 0;
  margin: 10px 0;
  font-size: 16px;
  font-weight: bold;
  background: #f0f0f0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.button:hover {
  background: #e2e2e2;
}
</style>
