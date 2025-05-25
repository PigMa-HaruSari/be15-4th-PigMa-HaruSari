<template>
  <div class="modal-overlay" @click.self="emit('close')">
    <div class="modal">
      <h3 class="modal-title">{{ title }}</h3>
      <p class="modal-message">{{ message }}</p>
      <div class="modal-actions">
        <button class="btn cancel" @click="emit('close')">취소</button>
        <button class="btn confirm" @click="emit('confirm')">삭제</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  title: {
    type: String,
    default: '정말 삭제하시겠습니까?'
  },
  message: {
    type: String,
    default: '삭제하면 되돌릴 수 없습니다.'
  }
})

const emit = defineEmits(['confirm', 'close'])
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
.modal {
  background: white;
  padding: 24px;
  border-radius: 12px;
  width: 360px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.modal-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
}
.modal-message {
  font-size: 14px;
  color: #444;
  margin-bottom: 20px;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  border: none;
}
.btn.cancel {
  background-color: #eee;
  color: #333;
}
.btn.confirm {
  background-color: #FF6B6B;
  color: white;
}
</style>
