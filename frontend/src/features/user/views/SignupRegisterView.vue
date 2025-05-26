<template>
  <Header/>
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="logo-box">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
      </div>
      <div class="subtitle">회원가입을 위해 정보를 입력해주세요</div>
      <div class="static-text">사용자 이메일: {{ email }}</div>
      <div class="input-box">
        <input type="text" v-model="nickname" placeholder="닉네임 입력" />
      </div>
      <div class="input-box">
        <select v-model="gender">
          <option value="NONE">성별 선택</option>
          <option value="MALE">남성</option>
          <option value="FEMALE">여성</option>
        </select>
      </div>
      <div class="checkbox-row">
        <input type="checkbox" id="agree" v-model="agree" />
        <label class="consent-text" for="agree">알림 수신에 동의합니다</label>
      </div>
      <div class="subtitle">일정의 카테고리를 추가하세요</div>
      <div id="category-container">
        <div class="form-group" v-for="(category, index) in categories" :key="index">
          <input type="text" v-model="categories[index]" placeholder="카테고리 입력" />
          <button
            :class="[
              index === categories.length - 1
                ? (!categories[index].trim() || categories.length >= maxCategory)
                  ? 'delete-btn'  // 추가인데 비어있고 5개라면 회색
                  : ''            // 추가 버튼 정상 상태
              : 'delete-btn'     // 삭제 버튼
            ]"
            :disabled="index === categories.length - 1 && (!categories[index].trim() || categories.length >= maxCategory)"
            @click="handleCategory(index)"
          >
            {{ index === categories.length - 1 ? '추가' : '삭제' }}
          </button>
        </div>
      </div>
      <div class="input-box">
        <button class="submit-btn" @click="submit">가입하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Header from '@/components/layout/Header.vue';
import { useSignupStore } from '@/stores/signupStore';
import { registerKakaoUser, registerUser } from '@/features/user/api';
import { showErrorToast, showSuccessToast } from '@/utill/toast';

const router = useRouter();
const signupStore = useSignupStore();

const email = computed(() => signupStore.email);
const password = computed(() => signupStore.password);
const nickname = ref('');
const gender = ref('NONE');
const agree = ref(false);
const categories = ref(['']);
const maxCategory = 5;

onMounted(() => {
  console.log("email.value : ", email.value)
  console.log("signupStore.provider : ", signupStore.provider)
  console.log("password.value : ", password.value)
  nickname.value = signupStore.nickname || '';
  if (!email.value || (signupStore.provider === 'LOCAL' && !password.value)) {
    showErrorToast('올바른 가입 절차를 따라야 합니다.');
    router.replace('/signup');
  }
});

const handleCategory = (index) => {
  if (index === categories.value.length - 1) {
    if (categories.value.length >= maxCategory) return;
    if (!categories.value[index].trim()) return;
    categories.value.push('');
  } else {
    categories.value.splice(index, 1);
  }
};

const submit = async () => {
  if (!nickname.value.trim()) {
    showErrorToast('닉네임을 입력해주세요.');
    return;
  }
  const validCategories = categories.value.filter((c) => c.trim());
  if (validCategories.length === 0) {
    showErrorToast('카테고리를 1개 이상 입력해주세요.');
    return;
  }

  signupStore.setFinalInfo({
    nicknameValue: nickname.value,
    genderValue: gender.value,
    consent: agree.value,
    categories: validCategories,
  });

  try {
    const payload = signupStore.getSignupPayload();
    console.log("  >> payload : ", payload)

    if (signupStore.provider === 'KAKAO') {
      await registerKakaoUser(payload);
    } else {
      await registerUser(payload);
    }

    showSuccessToast('회원가입이 완료되었습니다!');
    await router.push('/login');
  } catch (e) {
    const msg = e.response?.data?.message || '회원가입에 실패했습니다.';
    showErrorToast(msg);
  }
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
.static-text {
  margin-bottom: 16px;
  font-size: 14px;
  color: #333;
}
#category-container {
  width: 100%;
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
.form-group .delete-btn {
  background-color: #ccc;
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
.checkbox-row {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  justify-content: center;
  margin-bottom: 40px;
}
.checkbox-row label {
  font-size: 14px;
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
