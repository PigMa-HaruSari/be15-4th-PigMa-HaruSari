import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useSignupStore = defineStore('signup', () => {
  const email = ref('');
  const password = ref('');
  const nickname = ref('');
  const gender = ref('NONE');
  const consentPersonalInfo = ref(false);
  const categoryList = ref([]);
  const provider = ref('LOCAL');

  function setBasicInfo({ emailValue, passwordValue }) {
    email.value = emailValue;
    password.value = passwordValue;
    provider.value = 'LOCAL';
  }

  function setKakaoInfo({ emailValue, nicknameValue }) {
    email.value = emailValue;
    nickname.value = nicknameValue;
    password.value = ''; // 카카오는 비밀번호 없음
    provider.value = 'KAKAO';
  }

  function setFinalInfo({ nicknameValue, genderValue, consent, categories }) {
    nickname.value = nicknameValue;
    gender.value = genderValue;
    consentPersonalInfo.value = consent;
    categoryList.value = categories;
  }

  function getSignupPayload() {
    return {
      email: email.value,
      password: password.value,
      nickname: nickname.value,
      gender: gender.value,
      consentPersonalInfo: consentPersonalInfo.value,
      categoryList: categoryList.value.map(name => ({ categoryName: name }))
    };
  }

  return {
    email,
    password,
    nickname,
    gender,
    consentPersonalInfo,
    categoryList,
    provider,
    setBasicInfo,
    setKakaoInfo,
    setFinalInfo,
    getSignupPayload
  };
});
