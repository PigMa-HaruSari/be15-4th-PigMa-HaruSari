<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h3>{{ isEdit ? '카테고리 수정' : '카테고리 추가' }}</h3>
      <form @submit.prevent="submitCategory">
        <input type="text" v-model="categoryName" placeholder="카테고리를 입력하세요" required />

        <!-- 색상 선택 -->
        <div class="color-picker">
          <label>색상 선택</label>
          <div class="colors">
            <div
                class="color-swatch"
                v-for="color in colors"
                :key="color"
                :style="{ backgroundColor: color }"
                :class="{ selected: color === categoryColor }"
                @click="categoryColor = color"
            ></div>
          </div>
        </div>

        <!-- 사용자 정의 색상 입력 -->
        <div class="custom-color">
          <label>직접 선택 </label>
          <input type="color" v-model="categoryColor" />
        </div>

        <button type="submit">{{ isEdit ? '수정하기' : '등록하기' }}</button>
        <button type="button" @click="$emit('close')">취소</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { createCategory, updateCategory } from '@/features/category/categoryApi.js'

const emit = defineEmits(['close', 'created'])

const props = defineProps({
  isEdit: Boolean,
  editData: Object
})

const categoryName = ref('')
const categoryColor = ref('#FFD8BE')
const colors = ['#FFD8BE', '#CDB4DB', '#B5EAD7', '#FFF1A8', '#FF6B6B', '#33FF57', '#3357FF', '#888888']

const closeModal = () => {
  emit('close')
}


watch(() => props.editData, (newVal) => {
  if (props.isEdit && newVal) {
    console.log('[DEBUG] editData:', newVal); // ✅ 이거 넣어보면 확실해져요
    categoryName.value = newVal.title
    categoryColor.value = newVal.color
  }
}, { immediate: true })

const submitCategory = async () => {
  try {
    if (props.isEdit) {
      await updateCategory(props.editData.categoryId, {
        categoryName: categoryName.value,
        color: categoryColor.value,
      });
    } else {
      const response = await createCategory({
        categoryName: categoryName.value,
        color: categoryColor.value,
      });

      if (response.status === 201) {
        emit('created', {
          title: categoryName.value,
          color: categoryColor.value,
          categoryId: response.data.data.categoryId,
          completed: false,
        });
      }
    }

    categoryName.value = '';
    categoryColor.value = '#FFD8BE';
    emit('close');
  } catch (error) {
    console.error('카테고리 저장 오류:', error);
    alert('카테고리 저장에 실패했습니다.');
  }
}
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
  width: 100%;
  padding: 0.8rem 1.2rem;
  font-size: 1rem;
  box-sizing: border-box; /* ✅ 너비 초과 방지 */
  font-weight: 500;
  border: none;
  border-radius: 14px;
  background-color: #f5f5f5;
  color: #333;
  transition: all 0.25s ease;
  box-shadow: inset 0 0 0 1px #ddd;
}

input[type="text"]::placeholder {
  color: #aaa;
  font-weight: 400;
}

input[type="text"]:focus {
  outline: none;
  background-color: #fff;
  box-shadow: 0 0 0 3px rgba(147, 129, 255, 0.3);
}


.color-picker {
  display: flex;
  flex-direction: column;
}

.colors {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.color-swatch {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
  transition: transform 0.2s;
  border: 2px solid transparent;
}

.color-swatch:hover {
  transform: scale(1.2);
}

.color-swatch.selected {
  border: 3px solid #555;
  box-shadow: 0 0 0 2px white, 0 0 6px rgba(0,0,0,0.4);
}

.custom-color {
  margin-top: 1rem;
  text-align: center;
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