<template>
  <Header/>
  <div class="main-wrapper">
    <div class="signup-box">
      <div class="section">
        <p class="form-text">마이페이지</p>
      </div>

      <div class="section">
        <label class="form-label">이메일</label>
        <div class="input-box">
          <input type="email" :value="email" disabled />
        </div>
      </div>

      <div class="section">
        <label class="form-label">닉네임</label>
        <div class="input-box">
          <input type="text" v-model="nickname" />
        </div>
      </div>

      <div class="section">
        <label class="form-label">성별</label>
        <div class="input-box">
          <select v-model="gender">
            <option value="MALE">남성</option>
            <option value="FEMALE">여성</option>
            <option value="NONE">선택 안 함</option>
          </select>
        </div>
      </div>

      <div class="section">
        <label class="form-label">알림 수신 여부</label>
        <div class="input-box checkbox-row">
          <input type="checkbox" id="subscribe" v-model="agreeAlarm" />
          <label for="subscribe">동의합니다</label>
        </div>
      </div>

      <div class="input-box">
        <button class="submit-btn" @click="updateProfile">회원 정보 수정</button>
      </div>

      <hr />

      <div class="section">
        <p class="form-label">비밀번호 변경</p>
        <div class="input-box">
          <input type="password" v-model="currentPassword" placeholder="현재 비밀번호 입력" />
        </div>
        <div class="input-box">
          <input type="password" v-model="newPassword" placeholder="새 비밀번호 입력" />
        </div>
        <div class="input-box">
          <input type="password" v-model="newPasswordCheck" placeholder="새 비밀번호 확인" />
        </div>
        <div class="input-box">
          <button class="submit-btn" @click="updatePassword">비밀번호 변경</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from '@/components/layout/Header.vue';
import { fetchMyPageProfile, updateUserProfile, updateUserPassword } from '@/features/user/api';
import { showErrorToast, showSuccessToast } from '@/utill/toast';

const email = ref('');
const nickname = ref('');
const gender = ref('NONE');
const agreeAlarm = ref(false);

const currentPassword = ref('');
const newPassword = ref('');
const newPasswordCheck = ref('');

onMounted(async () => {
  try {
    const { data } = await fetchMyPageProfile();
    const profile = data.data;
    email.value = profile.email;
    nickname.value = profile.nickname;
    gender.value = profile.gender;
    agreeAlarm.value = profile.consentPersonalInfo;
  } catch (err) {
    showErrorToast('회원 정보를 불러오는 데 실패했습니다.');
  }
});

const updateProfile = async () => {
  try {
    await updateUserProfile({
      nickname: nickname.value,
      gender: gender.value,
      consentPersonalInfo: agreeAlarm.value
    });
    showSuccessToast('회원 정보가 성공적으로 수정되었습니다.');
  } catch (err) {
    showErrorToast('회원 정보 수정에 실패했습니다.');
  }
};

const updatePassword = async () => {
  if (!currentPassword.value || !newPassword.value || !newPasswordCheck.value) {
    return showErrorToast('모든 비밀번호 항목을 입력해주세요.');
  }
  if (newPassword.value !== newPasswordCheck.value) {
    return showErrorToast('새 비밀번호가 일치하지 않습니다.');
  }
  if (newPassword.value.length < 10) {
    return showErrorToast('새 비밀번호는 10자 이상이어야 합니다.');
  }
  try {
    await updateUserPassword({
      currentPassword: currentPassword.value,
      newPassword: newPassword.value,
      confirmPassword: newPasswordCheck.value
    });
    showSuccessToast('비밀번호가 성공적으로 변경되었습니다.');
    currentPassword.value = '';
    newPassword.value = '';
    newPasswordCheck.value = '';
  } catch (err) {
    const msg = err.response?.data?.message || '비밀번호 변경에 실패했습니다.';
    showErrorToast(msg);
  }
};
</script>

<style scoped>
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: calc(100vh - 85px);
  background: #f9f9f9;
  padding-top: 120px;
}
.signup-box {
  width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.section {
  width: 100%;
  margin-bottom: 24px;
}
.form-label {
  font-weight: bold;
  margin-bottom: 8px;
  display: block;
  text-align: left;
}
.form-text {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 24px;
  text-align: center;
  color: #333;
}
.input-box {
  width: 100%;
  margin-bottom: 12px;
}
.input-box input:not([type="checkbox"]),
.input-box select,
.input-box button.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}
.input-box .submit-btn {
  font-size: 16px;
  font-weight: bold;
  background: #f0f0f0;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}
.input-box .submit-btn:hover {
  background: #e2e2e2;
}
.checkbox-row {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
}
</style>
