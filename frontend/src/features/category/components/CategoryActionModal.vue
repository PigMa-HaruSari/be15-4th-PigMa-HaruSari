<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h3>{{ category.title }}</h3>
      <div class="actions">
        <button
            :class="category.completed ? 'cancel-complete-btn' : 'complete-btn'"
            @click="markComplete"
        >
          {{ category.completed ? '🔁 완료 처리 취소' : '✅ 완료 처리' }}
        </button>
        <button @click="editCategory">✏️ 수정</button>
        <button @click="deleteCategory">🗑 삭제</button>
        <button @click="close">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  category: Object
})

const emit = defineEmits(['close', 'edit', 'delete', 'complete', 'cancel-complete'])

const close = () => emit('close')
const editCategory = () => emit('edit', props.category)
const deleteCategory = () => emit('delete', props.category)
const markComplete = () => {
  if (props.category.completed) {
    emit('cancel-complete', props.category)
  } else {
    emit('complete', props.category)
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 300px;
  text-align: center;
}

.actions button {
  display: block;
  margin: 0.5rem auto;
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  width: 100%;
}

.actions button.complete-btn {
  background-color: #4CAF50; /* 진한 초록 */
  color: white;
}
.actions button.complete-btn:hover {
  background-color: #45a049;
}

.actions button.cancel-complete-btn {
  background-color: #FF9800; /* 주황 */
  color: white;
}
.actions button.cancel-complete-btn:hover {
  background-color: #e68900;
}

.actions button:nth-child(2) {
  background-color: #FFD8BE;
  color: black;
}
.actions button:nth-child(3) {
  background-color: #FF6B6B;
  color: white;
}
.actions button:nth-child(4) {
  background-color: #ccc;
  color: black;
}
</style>
