<template>
  <div class="form-overlay">
    <div class="form-container">
      <h3>{{ mode === 'edit' ? '✏️ 자동화 일정 수정' : '➕ 자동화 일정 추가' }}</h3>

      <form @submit.prevent="handleSubmit" class="form-body">
        <label>
          제목
          <input v-model="form.title" required placeholder="일정 제목 입력" />
        </label>

        <label>
          카테고리 선택
          <div class="custom-select-wrapper">
            <select v-model="form.categoryId" required class="custom-select">
              <option value="" disabled>카테고리를 선택하세요</option>
              <option
                  v-for="category in availableCategories"
                  :key="category.categoryId"
                  :value="category.categoryId"
              >
                {{ category.categoryName }}
              </option>
            </select>
            <span
                v-if="selectedCategoryColor"
                class="category-color-indicator"
                :style="{ backgroundColor: selectedCategoryColor }"
            ></span>
          </div>
        </label>

        <label>
          반복 종료일
          <input type="date" v-model="form.repeatEndDate" required />
        </label>

        <label>
          반복 주기
          <select v-model="form.repeatCycle" required>
            <option value="DAILY">매일</option>
            <option value="WEEKLY">매주</option>
            <option value="MONTHLY">매월</option>
          </select>
        </label>

        <div v-if="form.repeatCycle === 'WEEKLY'" class="weekday-options">
          <label v-for="day in weekdays" :key="day.code" class="weekday-checkbox">
            <input type="checkbox" :value="day.code" v-model="form.weekdays" />
            {{ day.label }}
          </label>
        </div>

        <div class="form-actions">
          <button type="submit" class="save-btn">저장</button>
          <button type="button" class="cancel-btn" @click="$emit('close')">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, watch } from 'vue'
import { createAutomationSchedule, updateAutomationSchedule } from '@/features/main/automationScheduleApi.js'
import { useToast } from 'vue-toastification'

const props = defineProps({
  mode: String,
  initialData: Object,
  categories: Array
})
const emit = defineEmits(['close'])
const toast = useToast()

const weekdays = [
  { code: 'MO', label: '월' },
  { code: 'TU', label: '화' },
  { code: 'WE', label: '수' },
  { code: 'TH', label: '목' },
  { code: 'FR', label: '금' },
  { code: 'SA', label: '토' },
  { code: 'SU', label: '일' },
]

const form = reactive({
  id: null,
  title: '',
  categoryId: null,
  repeatEndDate: '',
  repeatCycle: 'DAILY',
  weekdays: []
})

const availableCategories = computed(() => {
  return props.categories?.filter(c => !c.completed) || []
})

const selectedCategoryColor = computed(() => {
  const selected = availableCategories.value.find(c => c.categoryId === form.categoryId)
  return selected?.color || null
})

watch(
    () => props.initialData,
    (newVal) => {
      if (props.mode === 'edit' && newVal) {
        form.id = newVal.automationScheduleId || null
        form.title = newVal.automationScheduleContent || ''
        form.categoryId = newVal.categoryId || null
        form.repeatEndDate = newVal.endDate || ''
        form.repeatCycle = newVal.repeatType || 'DAILY'
        form.weekdays = newVal.repeatWeekdays ? newVal.repeatWeekdays.split(',') : []
      } else {
        // 생성 모드면 초기화
        form.title = ''
        form.categoryId = null
        form.repeatEndDate = ''
        form.repeatCycle = 'DAILY'
        form.weekdays = []
      }
    },
    { immediate: true }
)

const handleSubmit = async () => {
  try {
    const today = new Date()
    const todayDay = today.getDate()

    const payload = {
      automationScheduleContent: form.title,
      categoryId: form.categoryId,
      endDate: form.repeatEndDate,
      repeatType: form.repeatCycle,
      repeatWeekdays: null,
      repeatMonthday: null
    }

    if (form.repeatCycle === 'WEEKLY') {
      payload.repeatWeekdays = form.weekdays.join(',')
      payload.repeatMonthday = null
    } else if (form.repeatCycle === 'MONTHLY') {
      payload.repeatWeekdays = null
      payload.repeatMonthday = todayDay
    } else {
      payload.repeatWeekdays = null
      payload.repeatMonthday = null
    }

    if (props.mode === 'create') {
      await createAutomationSchedule(payload)
      toast.success('✅ 자동화 일정이 생성되었습니다!')
    } else {
      await updateAutomationSchedule(form.id, payload)
      toast.success('✏️ 자동화 일정이 수정되었습니다!')
    }

    emit('close')
  } catch (e) {
    toast.error('❌ 저장에 실패했습니다')
    console.error(e)
  }
}
</script>




<style scoped>
.form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1100;
}
.form-container {
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  width: 400px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}
h3 {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 16px;
  color: #4D4DFF;
  text-align: center;
}
.form-body label {
  display: block;
  margin-bottom: 14px;
  font-size: 14px;
  color: #333;
}
input,
select {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  box-sizing: border-box;
}
.custom-select-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}
.custom-select {
  flex-grow: 1;
  appearance: none;
  background: #f5f5ff;
  border: 1px solid #bbb;
  border-radius: 8px;
  padding: 8px;
  font-size: 14px;
  color: #333;
}
.category-color-indicator {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin-left: 10px;
  border: 1px solid #999;
}
.weekday-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 10px 0;
}
.weekday-checkbox {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #333;
  background: #f3f3f3;
  padding: 6px 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}
.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
.save-btn {
  background-color: #4D96FF;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}
.cancel-btn {
  background-color: #ddd;
  color: #333;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}
</style>