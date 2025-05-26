<script setup>
import { onMounted, onUnmounted } from 'vue';
import { connectSSE, closeSSE } from '@/utill/connectSSE'; // ✅ 추가
import { emitter } from '@/utill/emitter.js';
import { useToast } from 'vue-toastification';
import router from "@/router/index.js";

const toast = useToast();

const handleNotification = (data) => {
  console.log('📥 [handleNotification] 수신:', data);

  const message = typeof data === 'object' && data.message
      ? data.message
      : typeof data === 'string'
          ? data
          : '[❗ 유효한 알림 메시지 없음]';

  toast.info(`🔔 ${message}`, {
    timeout: 5000,
    position: 'top-right',
  });
};

// 🥚 이스터에그 키 입력 감지 (↑↑↓↓←→←→)
const secretCode = [38, 38, 40, 40, 37, 39, 37, 39]
let inputBuffer = []

const handleKeyDown = (e) => {
  inputBuffer.push(e.keyCode)
  if (inputBuffer.length > secretCode.length) {
    inputBuffer.shift()
  }

  if (inputBuffer.join() === secretCode.join()) {
    toast.success('🎉 이스터에그 발견!')
    router.push('/easterEgg')
  }
}


onMounted(() => {
  connectSSE(); // ✅ SSE 연결 시도
  emitter.on('notification', handleNotification);
  window.addEventListener('keydown', handleKeyDown)
});

onUnmounted(() => {
  closeSSE(); // ✅ 연결 종료
  emitter.off('notification', handleNotification);
  window.removeEventListener('keydown', handleKeyDown)
});
</script>

<template>
  <RouterView />
</template>
