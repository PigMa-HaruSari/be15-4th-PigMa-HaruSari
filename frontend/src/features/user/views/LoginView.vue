<template>
  <Header />
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="logo-box">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
      </div>
      <div class="subtitle">로그인을 위해 정보를 입력해주세요</div>
      <div class="input-box">
        <input v-model="email" type="text" placeholder="이메일 입력" @keyup.enter="handleLogin" />
      </div>
      <div class="input-box">
        <input v-model="password" type="password" placeholder="비밀번호 입력" @keyup.enter="handleLogin" />
      </div>
      <div class="input-box">
        <button class="login-btn" @click="handleLogin">로그인</button>
      </div>
      <div class="input-box">
        <button class="kakao-login-btn" @click="goToKakaoLogin">카카오로 로그인하기</button>
      </div>
      <div class="link-group">
        <div class="link-row">
          <router-link to="/signup">회원가입</router-link>
        </div>
        <div class="link-row">
          비밀번호 재설정
        </div>
      </div>
      <p class="error-message" v-if="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { useRouter, useRoute, RouterLink } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import { loginUser } from '@/features/user/api';
import Header from '@/components/layout/Header.vue';

const email = ref('');
const password = ref('');
const error = ref('');
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const handleLogin = async () => {
  try {
    const { data } = await loginUser({
      email: email.value, password: password.value
    });

    const userData = data.data;
    userStore.setUser(userData);

    // 탈퇴한 회원일 경우 즉시 로그아웃 + 메시지 출력
    if (userStore.userDeletedAt) {
      userStore.logout();
      error.value = '이미 탈퇴한 회원입니다.';
      return;
    }

    // nextTick을 사용해 상태 반영 이후 라우터 실행
    await nextTick();

    const redirectPath = route.query.redirect || '/';
    const matchedRoute = router.resolve(redirectPath);

    if (!matchedRoute.matched.length) {
      console.warn('유효하지 않은 redirectPath:', redirectPath);
      await router.push('/');
    } else if (redirectPath !== router.currentRoute.value.fullPath) {
      console.log("라우터 이동:", redirectPath);
      setTimeout(() => {
        router.push(redirectPath);
      }, 0); // 이벤트 루프 한 틱 뒤에 실행
    }
  } catch (err) {
    error.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
  }
};

const goToKakaoLogin = () => {
  const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID;
  const KAKAO_LOGIN_REDIRECT_URI = import.meta.env.VITE_KAKAO_LOGIN_REDIRECT_URI;

  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${KAKAO_CLIENT_ID}&redirect_uri=${KAKAO_LOGIN_REDIRECT_URI}`;
  window.location.href = kakaoAuthUrl;
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
.link-group {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 0 100px;
  box-sizing: border-box;
}

.link-group a {
  font-size: 14px;
  color: #716aca;
  text-decoration: none;
}

.link-row {
  display: flex;
  margin: 10px;
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
