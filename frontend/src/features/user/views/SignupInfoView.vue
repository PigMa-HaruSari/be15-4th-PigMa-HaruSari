<template>
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="logo-box">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
      </div>
      <div class="subtitle">이메일 인증을 통해 가입을 시작해보세요</div>
      <div class="form-group">
        <input type="email" v-model="email" placeholder="이메일 입력" />
        <button @click="sendCode" :disabled="codeSent">
          {{ codeSent ? '전송됨' : '인증번호 전송' }}
        </button>
      </div>
      <div class="input-box" v-if="codeSent">
        <input type="text" v-model="code" placeholder="인증번호 입력" />
      </div>

      <div class="input-box" v-if="codeSent">
        <button class="submit-btn" @click="verifyCode">인증번호 확인</button>
      </div>

      <div class="input-box">
        <input type="password" v-model="password" placeholder="비밀번호 입력" />
      </div>
      <div class="input-box">
        <input type="password" v-model="confirmPassword" placeholder="비밀번호 확인" />
      </div>

      <div class="input-box">
        <button class="submit-btn" @click="goNextStep">가입하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Header from '@/components/layout/Header.vue';
import { sendEmailCode, verifyEmailCode } from '@/features/user/api';
import { useSignupStore } from '@/stores/signupStore.js';
import { showErrorToast, showSuccessToast } from '@/utill/toast.js';

const router = useRouter();
const signupStore = useSignupStore();

const email = ref('');
const code = ref('');
const password = ref('');
const confirmPassword = ref('');
const codeSent = ref(false);
const codeVerified = ref(false);

const sendCode = async () => {
  try {
    await sendEmailCode(email.value);
    codeSent.value = true;
    showSuccessToast('인증번호가 전송되었습니다.');
  } catch (e) {
    showErrorToast('인증번호 전송에 실패했습니다.');
  }
};

const verifyCode = async () => {
  try {
    await verifyEmailCode(email.value, code.value);
    codeVerified.value = true;
    showSuccessToast('이메일 인증이 완료되었습니다.');
  } catch (e) {
    showErrorToast('인증번호가 올바르지 않습니다.');
  }
};

const goNextStep = () => {
  if (!email.value || !password.value || !confirmPassword.value) {
    showErrorToast('모든 항목을 입력해주세요.');
    return;
  }
  if (!codeVerified.value) {
    showErrorToast('이메일 인증을 완료해주세요.');
    return;
  }
  if (password.value.length < 10 || password.value.length > 20) {
    showErrorToast('비밀번호는 10자 이상 20자 이하로 입력해주세요.');
    return;
  }
  if (password.value !== confirmPassword.value) {
    showErrorToast('비밀번호가 일치하지 않습니다.');
    return;
  }
  signupStore.setBasicInfo({ emailValue: email.value, passwordValue: password.value });
  router.push('/signup/register');
};
</script>

<style scoped>
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 85px);
  background: #f9f9f9;
}
.signup-box {
  width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
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
.form-group {
  display: flex;
  width: 100%;
  margin-bottom: 12px;
}
.form-group input {
  padding: 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  flex: 1;
}
.form-group button {
  margin-left: 8px;
  padding: 12px 16px;
  font-size: 14px;
  border: none;
  border-radius: 8px;
  background-color: #716aca;
  color: #fff;
  cursor: pointer;
}
.input-box {
  width: 100%;
  margin-bottom: 12px;
}
.input-box input,
.input-box button.submit-btn {
  width: 100%;
  box-sizing: border-box;
}
.input-box input {
  padding: 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.submit-btn {
  padding: 14px;
  font-size: 16px;
  font-weight: bold;
  background: #f0f0f0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 4px;
}
.submit-btn:hover {
  background: #e2e2e2;
}
</style>
