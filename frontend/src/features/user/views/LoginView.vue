<template>
  <Header />
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="logo-box">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
      </div>
      <div class="subtitle">로그인을 위해 정보를 입력해주세요</div>
      <div class="link-row">
        <router-link to="/signup">회원가입</router-link>
      </div>
      <div class="input-box">
        <input v-model="email" type="text" placeholder="이메일 입력" @keyup.enter="handleLogin" />
      </div>
      <div class="input-box">
        <input v-model="password" type="password" placeholder="비밀번호 입력" @keyup.enter="handleLogin" />
      </div>
      <div class="link-row">
        <router-link to="/reset-password">비번 재설정</router-link>
      </div>
      <div class="input-box">
        <button class="login-btn" @click="handleLogin">로그인</button>
      </div>
      <div class="input-box">
        <button class="kakao-login-btn">카카오로 로그인하기</button>
      </div>
      <p class="error-message" v-if="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter, useRoute, RouterLink } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import api from '@/lib/axios.js';
import Header from '@/components/layout/Header.vue';

const email = ref('');
const password = ref('');
const error = ref('');
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const handleLogin = async () => {
  try {
    const res = await api.post('/auth/login', {
      email: email.value,
      password: password.value
    });

    const userData = res.data.data;
    console.log('[USER DATA]', userData);

    userStore.setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('accessToken', userData.accessToken);

    const redirectPath = route.query.redirect || '/';
    if (redirectPath !== router.currentRoute.value.fullPath) {
      await router.push(redirectPath);
    }
  } catch (err) {
    console.error('[LOGIN ERROR]', err);
    error.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
  }
};
</script>

<style scoped>
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px);
  background: #f9f9f9;
}
.signup-box {
  width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.logo-box img {
  height: 80px;
  margin-bottom: 16px;
}
.subtitle {
  margin-bottom: 12px;
  font-size: 16px;
  color: #555;
}
.input-box {
  width: 100%;
  margin-bottom: 12px;
}
.input-box input {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}
.login-btn,
.kakao-login-btn {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.login-btn {
  background: #f0f0f0;
  transition: background 0.2s ease;
  margin-bottom: 12px;
}
.login-btn:hover {
  background: #e2e2e2;
}
.kakao-login-btn {
  background: #fee500;
  color: #3c1e1e;
}
.link-row {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-bottom: 12px;
}
.link-row a {
  font-size: 14px;
  color: #716aca;
  text-decoration: none;
}
.error-message {
  color: red;
  font-size: 13px;
  text-align: center;
  margin-top: 8px;
}
</style>
