<template>
  <div v-if="visible" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-container">
      <div class="modal-header">
        <h2>📅 자동화 일정 관리</h2>
        <button class="close-btn" @click="$emit('close')">✖</button>
      </div>

      <div class="modal-content">
        <!-- 일정 목록 -->
        <div v-if="Array.isArray(automationList) && automationList.length">
          <div
              class="automation-item"
              v-for="item in automationList"
              :key="item.automationScheduleId"
          >
            <div class="info">
              <h3>{{ item.automationScheduleContent }}</h3>
              <p><strong>카테고리:</strong> {{ item.categoryName }}</p>
              <p><strong>반복:</strong> {{ item.repeatType }}</p>
              <p><strong>종료일:</strong> {{ item.endDate }}</p>
            </div>
            <div class="actions">
              <button class="edit-btn" @click="handleEdit(item)">수정</button>
              <button class="delete-btn" @click="handleDelete(item.automationScheduleId)">삭제</button>
            </div>
          </div>
        </div>
        <div v-else class="empty-message">📭 등록된 자동화 일정이 없습니다.</div>

        <!-- 추가 버튼 -->
        <div class="footer">
          <button class="create-btn" @click="handleCreate">➕ 새 자동화 규칙 추가</button>
        </div>
      </div>

      <!-- 폼 모달 -->
      <AutomationScheduleForm
          v-if="formVisible"
          :mode="formMode"
          :initialData="selectedItem"
          :categories="categories"
      @close="formVisible = false; fetchList()"
      />
      <ConfirmWithInputModal
          v-if="deleteConfirmVisible"
          :title="'자동화 일정을 삭제할까요?'"
          :message="'해당 자동화 규칙과 이후 생성될 모든 일정이 삭제됩니다. \n정말 삭제하시겠습니까? \n\n \'자동화 일정을 삭제하겠습니다\'를 입력하세요.'"
          :requiredText="'자동화 일정을 삭제하겠습니다'"
          @confirm="handleConfirmedDelete"
          @close="deleteConfirmVisible = false"
      />

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AutomationScheduleForm from './AutomationScheduleForm.vue'
import { fetchAutomationSchedules, deleteAutomationSchedule } from '@/features/main/automationScheduleApi.js'
import {fetchCategory} from "@/features/main/mainApi.js";
import ConfirmWithInputModal from "@/components/common/ConfirmWithInputModal.vue";

const props = defineProps({ visible: Boolean })
const emit = defineEmits(['close'])
const categories = ref([])


const automationList = ref([])
const formVisible = ref(false)
const formMode = ref('create')
const selectedItem = ref(null)
const deleteConfirmVisible = ref(false)
const scheduleIdToDelete = ref(null)


const fetchList = async () => {
  try {
    const res = await fetchAutomationSchedules()
    automationList.value = Array.isArray(res.data) ? res.data : []
    console.log('자동화 일정 응답:', res.data)
  } catch (e) {
    automationList.value = []
    console.error('자동화 일정 목록 조회 실패:', e)
  }
}

const handleCreate = () => {
  formMode.value = 'create'
  selectedItem.value = null
  formVisible.value = true
}

const handleEdit = (item) => {
  formMode.value = 'edit'
  selectedItem.value = item
  formVisible.value = true
}


const handleDelete = (id) => {
  scheduleIdToDelete.value = id
  deleteConfirmVisible.value = true
}

const handleConfirmedDelete = async () => {
  try {
    await deleteAutomationSchedule(scheduleIdToDelete.value)
    await fetchList()
  } catch (e) {
    console.error('❌ 삭제 오류:', e)
  } finally {
    deleteConfirmVisible.value = false
    scheduleIdToDelete.value = null
  }
}

onMounted(async () => {
  await fetchList()

  try {
    const res = await fetchCategory()
    console.log('카테고리 응답:', res.data)
    const list = res.data?.data
    categories.value = Array.isArray(list) ? list : []
  } catch (e) {
    console.error('카테고리 목록 조회 실패:', e)
    categories.value = []
  }

})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-container {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  width: 640px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.modal-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #333;
}
.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #999;
}
.automation-item {
  background-color: #f9f9ff;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
}
.automation-item .info h3 {
  font-size: 18px;
  margin-bottom: 6px;
  color: #4D4DFF;
}
.actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.edit-btn,
.delete-btn {
  padding: 6px 12px;
  font-size: 14px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: white;
}
.edit-btn {
  background-color: #4D96FF;
}
.delete-btn {
  background-color: #FF6B6B;
}
.footer {
  text-align: right;
  margin-top: 24px;
}
.create-btn {
  background-color: #7C4DFF;
  color: white;
  padding: 10px 16px;
  font-size: 15px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}
.empty-message {
  text-align: center;
  font-size: 15px;
  color: #777;
  padding: 30px 0;
}


</style>
