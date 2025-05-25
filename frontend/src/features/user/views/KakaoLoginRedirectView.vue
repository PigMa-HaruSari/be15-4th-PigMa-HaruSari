<script setup>
import { useRouter, useRoute } from 'vue-router';
import { showErrorToast, showSuccessToast } from '@/utill/toast';
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import api from '@/lib/axios';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

onMounted(async () => {
  const code = route.query.code;
  if (!code) {
    showErrorToast('인가 코드가 존재하지 않습니다.');
    return router.replace('/login');
  }

  try {
    const res = await api.get(`/auth/social/login/kakao?code=${code}`);
    const userData = res.data.data;
    userStore.setUser(userData);
    showSuccessToast('카카오 로그인 성공!');
    router.push('/');
  } catch (e) {
    console.log(e);
    showErrorToast('카카오 로그인에 실패했습니다.');
    router.replace('/login');
  }
});
</script>
