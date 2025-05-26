<script setup>
import { onMounted, onUnmounted } from 'vue';
import { connectSSE, closeSSE } from '@/utill/connectSSE'; // ✅ 추가
import { emitter } from '@/utill/emitter.js';
import { useToast } from 'vue-toastification';

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


onMounted(() => {
  connectSSE(); // ✅ SSE 연결 시도
  emitter.on('notification', handleNotification);
});

onUnmounted(() => {
  closeSSE(); // ✅ 연결 종료
  emitter.off('notification', handleNotification);
});
</script>

<template>
  <RouterView />
</template>
