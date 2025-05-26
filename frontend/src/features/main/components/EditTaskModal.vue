<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal">
      <h2 class="modal-title">할 일 수정</h2>
      <form @submit.prevent="submit" class="modal-form">
        <div class="form-group">
          <span class="form-label">할 일 내용</span>
          <input
              type="text"
              v-model="form.scheduleContent"
              placeholder="수정할 내용을 입력하세요"
              required
              class="task-input"
          />
        </div>
        <div class="form-actions">
          <button type="button" class="btn cancel" @click="close">취소</button>
          <button type="submit" class="btn submit">수정 완료</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  task: Object
})
const emit = defineEmits(['close', 'update'])

const form = ref({
  scheduleContent: '',
  scheduleDate: ''
})

watch(() => props.task, (task) => {
  if (task) {
    form.value.scheduleContent = task.text || ''
    form.value.scheduleDate = task.scheduleDate || new Date().toISOString().split('T')[0]
  }
}, { immediate: true })

const close = () => emit('close')

const submit = () => {
  if (!form.value.scheduleContent.trim()) return

  emit('update', {
    scheduleId: props.task.scheduleId,
    categoryId: props.task.categoryId,
    scheduleContent: form.value.scheduleContent.trim(),
    scheduleDate: form.value.scheduleDate
  })
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}
.modal {
  background: #ffffff;
  padding: 28px 32px;
  border-radius: 16px;
  width: 400px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.modal-title {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  color: #333;
}
.modal-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.form-label {
  font-size: 14px;
  font-weight: 500;
  color: #555;
}
.task-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.btn.cancel {
  background-color: #f0f0f0;
  color: #555;
}
.btn.cancel:hover {
  background-color: #e0e0e0;
}
.btn.submit {
  background-color: #9381ff;
  color: white;
}
.btn.submit:hover {
  background-color: #7a6be3;
}
</style>
