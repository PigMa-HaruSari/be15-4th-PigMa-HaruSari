<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h3>카테고리 추가</h3>
      <form @submit.prevent="submitCategory">
        <input v-model="categoryName" placeholder="카테고리 이름" required />

        <!-- 대표 색상 선택 -->
        <div class="color-picker">
          <label>색상 선택</label>
          <div class="colors">
            <div
                class="color-swatch"
                v-for="color in colors"
                :key="color"
                :style="{ backgroundColor: color }"
                @click="categoryColor = color"
            ></div>
          </div>
        </div>

        <button type="submit">등록</button>
        <button type="button" @click="$emit('close')">취소</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { createCategory } from '@/features/category/categoryApi.js'  // 새로 만든 createCategory 함수

const emit = defineEmits(['close', 'created'])

const categoryName = ref('')
const categoryColor = ref('#FFD8BE')  // 기본 색상
const colors = ['#FFD8BE', '#CDB4DB', '#B5EAD7', '#FFF1A8']  // 대표 색상 목록

const submitCategory = async () => {
  try {
    // 카테고리 생성 요청
    const response = await createCategory({
      categoryName: categoryName.value,
      color: categoryColor.value,
    });

    if (response.status === 201) {
      emit('created', {
        title: categoryName.value,
        color: categoryColor.value,
        categoryId: response.data.data.categoryId,
        completed: false, // 기본값
      });
    }
    categoryName.value = '';
    categoryColor.value = '#FFD8BE';  // 초기 색상 리셋
    emit('close');
  } catch (error) {
    console.error('카테고리 생성 오류:', error);
    alert('카테고리 추가에 실패했습니다.');
  }
};

</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  min-width: 300px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

h3 {
  margin-bottom: 1rem;
  color: #333;
  text-align: center;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

input[type="text"] {
  padding: 0.6rem;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.color-picker {
  display: flex;
  flex-direction: column;
}

.colors {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.color-swatch {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
  transition: transform 0.2s;
}

.color-swatch:hover {
  transform: scale(1.2);
}

button {
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  background-color: #9381FF;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #7a6ee0;
}

button[type="button"] {
  background-color: #ddd;
}
</style>
