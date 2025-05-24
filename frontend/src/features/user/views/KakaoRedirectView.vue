<template>
  <div class="loading-wrapper">
    <p>카카오 인증 중입니다. 잠시만 기다려주세요...</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { getKakaoUserInfo } from '@/features/user/api';
import { useSignupStore } from '@/stores/signupStore';
import { showErrorToast } from '@/utill/toast';
console.log('--- script 진입')

const router = useRouter();
const route = useRoute();
const signupStore = useSignupStore();
console.log('--- signupStore: ', signupStore)


onMounted(async () => {
  console.log('--- onmounted 진입')
  const code = route.query.code;
  console.log('code: ', code)


  if (!code) {
    console.log('--- 인증 코드가 존재하지 않습니다.')
    showErrorToast('카카오 인증 코드가 존재하지 않습니다.');
    return router.replace('/signup');
  }
  console.log('--- if문 종료')


  try {
    console.log('---try문 진입')

    const res = await getKakaoUserInfo(code);
    console.log('res: ', res)

    const { email, nickname } = res.data.data;
    console.log('email: ', email)
    console.log('nickname: ', nickname)


    // store에 카카오 정보 저장
    signupStore.setKakaoInfo({ emailValue: email, nicknameValue: nickname });
    console.log('---signupStore.setKakaoInfo')


    // 가입 등록 페이지로 이동
    router.replace('/signup/register');
    console.log('---router: ', router)

  } catch (err) {
    console.log('---err: ', err)
    showErrorToast('카카오 인증에 실패했습니다. 다시 시도해주세요.');
    router.replace('/signup');
  }
});
</script>

<style scoped>
.loading-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  color: #555;
}
</style>
