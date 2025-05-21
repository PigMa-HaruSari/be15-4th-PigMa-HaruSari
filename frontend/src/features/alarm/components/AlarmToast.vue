<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const toasts = ref([])

function addToast(alarm) {
  toasts.value.push(alarm)
  setTimeout(() => {
    toasts.value.shift()
  }, 5000) // 5초 후 사라짐
}

// SSE 구독 (예시)
let eventSource = null

onMounted(() => {
  eventSource = new EventSource('/api/v1/alarm')
  eventSource.onmessage = (e) => {
    const alarm = JSON.parse(e.data)
    addToast(alarm)
  }
})

onBeforeUnmount(() => {
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<template>
  <div class="toast-container">
    <div
        class="toast"
        v-for="(toast, i) in toasts"
        :key="toast.id + i"
    >
      {{ toast.message }}
    </div>
  </div>
</template>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  width: 300px;
  z-index: 2000;
}

.toast {
  background: #444;
  color: white;
  padding: 12px 20px;
  margin-bottom: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
  animation: fadein 0.3s ease forwards;
}

@keyframes fadein {
  from { opacity: 0; transform: translateY(-10px);}
  to { opacity: 1; transform: translateY(0);}
}
</style>
