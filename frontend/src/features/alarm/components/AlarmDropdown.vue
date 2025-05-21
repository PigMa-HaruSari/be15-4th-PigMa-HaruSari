<!-- AlarmDropdown.vue -->
<script setup>
import { ref, onMounted } from 'vue'
const emit = defineEmits(['close'])

const alarms = ref([])
const isLoading = ref(true)
const error = ref(null)

onMounted(() => {
  // 예시: 1초 후에 알림 데이터 로드
  setTimeout(() => {
    // alarms.value = [] // ← 테스트용 빈 알림
    alarms.value = [
      { id: 1, message: '오늘 해야 할 일이 아직 남아 있어요.' },
      { id: 2, message: '주간 달성률이 70%입니다!' },
    ]
    isLoading.value = false
  }, 1000)
})
</script>

<template>
  <div class="notification-modal">
    <button class="close-btn" @click="emit('close')">닫기</button>
    <h3>알림</h3>
    <div v-if="isLoading">로딩 중...</div>
    <div v-else-if="error">{{ error }}</div>
    <ul v-else>
      <li v-for="alarm in alarms" :key="alarm.id">{{ alarm.message }}</li>
      <li v-if="alarms.length === 0">읽지 않은 알림이 없습니다.</li>
    </ul>
  </div>
</template>

<style scoped>
.notification-modal {
  position: absolute;
  top: 100%;
  left: 70%; /* 아이콘 오른쪽 끝 기준 */
  width: 250px;
  background: white;
  border: 1px solid #ccc;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  border-radius: 8px;
}

.close-btn {
  float: right;
  background: none;
  border: none;
  font-size: 14px;
  color: #888;
  cursor: pointer;
}

h3 {
  margin: 0 0 10px;
  font-size: 18px;
  color: #333;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  padding: 6px 0;
  font-size: 14px;
  color: #444;
}
</style>
