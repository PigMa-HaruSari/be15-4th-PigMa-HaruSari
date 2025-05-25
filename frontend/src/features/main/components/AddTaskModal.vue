<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal">
      <h2 class="modal-title">할 일 추가</h2>
      <form @submit.prevent="submit" class="modal-form">
        <label class="form-group">
          <span class="form-label">카테고리</span>
          <div class="custom-select-wrapper">
            <select v-model="form.categoryId" required class="custom-select">
              <option disabled value="">카테고리를 선택하세요</option>
              <option
                  v-for="cat in filteredCategories"
                  :key="cat.categoryId"
                  :value="cat.categoryId"
              >
                {{ cat.title }}
              </option>
            </select>
            <span v-if="form.categoryId" class="category-color-dot-inline" :style="{ backgroundColor: selectedCategoryColor }"></span>
          </div>
        </label>

        <div class="form-group">
          <span class="form-label">할 일 목록</span>
          <div
              v-for="(content, idx) in form.scheduleContents"
              :key="idx"
              class="task-item"
          >
            <input
                type="text"
                v-model="form.scheduleContents[idx]"
                placeholder="할 일을 입력하세요"
                required
                @keydown.enter.prevent="handleEnter(idx)"
            />
            <button type="button" class="remove-btn" @click="removeTask(idx)">✕</button>
          </div>
          <button type="button" class="btn add" @click="addTask">+ 할 일 추가</button>
        </div>

        <div class="form-actions">
          <button type="button" class="btn cancel" @click="close">취소</button>
          <button type="submit" class="btn submit">등록</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { createTask } from '@/features/main/mainApi'
import { useToast } from 'vue-toastification'

const props = defineProps({
  categories: Array,
  defaultDate: String,
})
const emit = defineEmits(['close', 'submitted'])

const toast = useToast()

const today = new Date().toISOString().split('T')[0]

const form = ref({
  categoryId: '',
  scheduleContents: [''],
  scheduleDate: today
})

const filteredCategories = computed(() => {
  return props.categories.filter(cat => !cat.completed)
})

const selectedCategoryColor = computed(() => {
  const cat = props.categories.find(cat => cat.categoryId === form.value.categoryId)
  return cat ? cat.color : '#ccc'
})

const addTask = () => {
  form.value.scheduleContents.push('')
}

const handleEnter = (index) => {
  if (form.value.scheduleContents[index].trim()) {
    addTask()
    nextTick(() => {
      const inputs = document.querySelectorAll('.task-item input')
      inputs[index + 1]?.focus()
    })
  }
}

const removeTask = (index) => {
  form.value.scheduleContents.splice(index, 1)
}

const close = () => emit('close')
/*
const submit = async () => {
  try {
    const promises = form.value.scheduleContents
        .filter(content => content.trim())
        .map(content =>
            createTask({
              categoryId: form.value.categoryId,
              scheduleContent: content,
              scheduleDate: form.value.scheduleDate,
              automationScheduleId: null
            })
        )
    await Promise.all(promises)
    toast.success('할 일들이 등록되었습니다!')
    emit('submitted')
    close()
  } catch (e) {
    toast.error('등록 중 오류가 발생했어요.')
  }
}*/
const submit = async () => {
  const trimmedContents = form.value.scheduleContents.map(c => c.trim()).filter(Boolean);

  if (!form.value.categoryId || form.value.categoryId === "") {
    toast.error("카테고리를 선택해 주세요.");
    return;
  }

  if (!form.value.scheduleDate) {
    toast.error("일정 날짜를 선택해 주세요.");
    return;
  }

  if (trimmedContents.length === 0) {
    toast.error("할 일을 한 개 이상 입력해 주세요.");
    return;
  }

  console.log("🔥 등록 데이터:", {
    categoryId: form.value.categoryId,
    scheduleDate: form.value.scheduleDate,
    scheduleContents: trimmedContents
  });

  try {
    const promises = trimmedContents.map(content =>
        createTask({
          categoryId: form.value.categoryId,
          scheduleContent: content,
          scheduleDate: form.value.scheduleDate,
          automationScheduleId: null
        })
    );
    console.log("✅ 전송할 payload 목록:", debugPayloads);

    await Promise.all(promises);
    toast.success("할 일들이 등록되었습니다!");
    emit("submitted");
    close();
  } catch (e) {
    console.error("❌ 등록 오류:", e);
    toast.error("등록 중 오류가 발생했어요.");
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
select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: white;
  font-size: 14px;
  font-family: inherit;
  appearance: none;
  background-image: url('data:image/svg+xml;utf8,<svg fill="%23666" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>');
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 16px 16px;
}
select:focus {
  outline: none;
  border-color: #9381ff;
  box-shadow: 0 0 0 2px rgba(147, 129, 255, 0.3);
}
.task-item {
  display: flex;
  gap: 8px;
  align-items: center;
}
.task-item input[type="text"] {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}
.remove-btn {
  background: #ffefef;
  border: 1px solid #e4a3a3;
  color: #cc3333;
  border-radius: 6px;
  padding: 4px 8px;
  cursor: pointer;
}
.btn.add {
  align-self: flex-start;
  background-color: #f2f2ff;
  color: #5a49d4;
  border: none;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.btn.add:hover {
  background-color: #e6e6ff;
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.custom-select-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
}
.custom-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  appearance: none;
  background: #fff url('data:image/svg+xml;utf8,<svg fill="%23666" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>') no-repeat right 10px center;
  background-size: 16px 16px;
}
.category-color-dot-inline {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  border: 2px solid #eee;
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
