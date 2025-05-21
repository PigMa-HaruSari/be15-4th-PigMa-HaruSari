<!--
<template>
  <div class="login-container">
    <div class="login-box">
      <h2>하루살이 로그인</h2>
      <input v-model="email" type="email" placeholder="이메일" />
      <input v-model="password" type="password" placeholder="비밀번호" />
      <button @click="handleLogin">로그인</button>
      <p class="error-message" v-if="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import api from '@/lib/api';
import { useRoute } from 'vue-router';


const email = ref('');
const password = ref('');
const error = ref('');
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// const handleLogin = async () => {
//   try {
//     const res = await api.post('/auth/login', {
//       email: email.value,
//       password: password.value
//     });
//     userStore.setUser(res.data); // 로그인 성공 시 userStore에 저장
//     router.push('/'); // 메인 페이지로 이동
//   } catch (err) {
//     error.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
//   }
// };
const handleLogin = async () => {
  try {
    const res = await api.post('/auth/login', {
      email: email.value,
      password: password.value
    });

    console.log(email.value, password.value);


    const userData = res.data.data;
    localStorage.setItem('user', JSON.stringify(userData));
    userStore.setUser(userData);

    // ✅ redirect 쿼리 파라미터가 있으면 해당 경로로 이동
/*    const redirectPath = route.query.redirect || '/';
    // router.push(redirectPath);
    if (redirectPath !== router.currentRoute.value.fullPath) {
      router.push(redirectPath);
    }*/
    const redirectPath = route.query.redirect;

    console.log('[로그인 응답]', res);
    console.log('[res.data]', res.data);
    if (redirectPath) {
      router.push(redirectPath);
    } else {
      router.push({ name: 'main' }); // 이름 기반으로 안전하게 이동
    }
  } catch (err) {

    console.error('[LOGIN ERROR]', err); // ✅ 로그 확인용 추가

    error.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
  }
  console.log('[REDIRECT PATH]', route.query.redirect);

};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f0f5;
}
.login-box {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 300px;
}
.login-box h2 {
  margin-bottom: 10px;
  text-align: center;
  color: #333;
}
.login-box input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}
.login-box button {
  padding: 10px;
  background-color: #9381FF;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 15px;
}
.login-box .error-message {
  color: red;
  font-size: 13px;
  text-align: center;
  margin-top: 8px;
}
</style>
-->
<template>
  <div class="login-container">
    <div class="login-box">
      <h2>하루살이 로그인</h2>
      <input v-model="email" type="email" placeholder="이메일" @keyup.enter="handleLogin" />
      <input v-model="password" type="password" placeholder="비밀번호" @keyup.enter="handleLogin" />
      <button @click="handleLogin">로그인</button>
      <p class="error-message" v-if="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import api from '@/lib/api';

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

    const redirectPath = route.query.redirect || '/';
    if (redirectPath !== router.currentRoute.value.fullPath) {
      router.push(redirectPath);
    }
  } catch (err) {
    console.error('[LOGIN ERROR]', err);
    error.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f0f5;
}
.login-box {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 300px;
}
.login-box h2 {
  margin-bottom: 10px;
  text-align: center;
  color: #333;
}
.login-box input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}
.login-box button {
  padding: 10px;
  background-color: #9381FF;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 15px;
}
.login-box .error-message {
  color: red;
  font-size: 13px;
  text-align: center;
  margin-top: 8px;
}
</style>
