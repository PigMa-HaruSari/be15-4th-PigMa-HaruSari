<template>
  <div>
    <Header />
    <div class="main-wrapper">
      <div class="main">
        <!-- 달력 영역 -->
        <div class="calendar-content">
          <div class="calendar-box">
            <div id="calendar" ref="calendarRef"></div>
          </div>
        </div>

        <!-- 할 일 + 회고 영역 -->
        <div class="right-content">
          <div class="today-task-box">
            <div class="today-task-header">
              <h4>오늘 할 일</h4>
              <div class="button-group">
                <button class="add-task-btn" @click="showAddTaskModal = true">할 일 추가</button>
                <button class="automation-btn" @click="showAutomationModal = true">자동화</button>
              </div>
            </div>

            <!-- 카테고리별 할 일 리스트 -->
            <div
                class="category"
                v-for="(category, index) in filteredCategories"
                :key="index"
            >
              <div class="category-title">
                <span class="category-tag" :style="{ backgroundColor: category.color }"></span>
                {{ category.title }}
              </div>

              <div
                  class="category-task"
                  v-for="(task, i) in category.tasks"
                  :key="i"
                  :style="getTaskStyle(category.color, task.completed)"
              >
                <input
                    type="checkbox"
                    v-model="task.completed"
                    @change="toggleTaskCompletion(task)"
                />
                <span class="task-text">{{ task.text }}</span>

                <div class="task-actions">
                  <button class="btn edit-btn" @click="openEditTaskModal(task)">수정</button>
                  <button class="btn delete-btn" @click="confirmDeleteTask(task)">삭제</button>
                </div>
              </div>
            </div>
          </div>

          <div class="review-box">
            <h4>회고</h4>

            <!-- 회고 존재하면서 오늘 작성 && 수정 중일 때 -->
            <div v-if="diary && isToday(diary.createdAt) && isEditing">
              <textarea v-model="reviewText" placeholder="오늘 하루는 어땠나요? 자유롭게 기록해보세요." />
              <div class="review-actions">
                <button @click="isEditing = false">취소</button>
                <button @click="updateExistingDiary">수정 완료</button>
              </div>
            </div>

            <!-- 회고 존재하면서 오늘 작성 && 수정 중 아님 -->
            <div v-else-if="diary && isToday(diary.createdAt)">
              <div class="readonly-diary">{{ diary.diaryContent }}</div>
              <div class="review-actions">
                <button @click="editDiary">수정</button>
                <button @click="deleteExistingDiary">삭제</button>
              </div>
            </div>

            <!-- 회고 존재하지만 오늘 아님 -->
            <div v-else-if="diary">
              <div class="readonly-diary">{{ diary.diaryContent }}</div>
            </div>

            <!-- 회고 없음이고 오늘일 경우에만 작성 가능 -->
            <div v-else-if="!diary && isSameDate(selectedDate, new Date())">
              <textarea v-model="reviewText" placeholder="오늘 하루는 어땠나요? 자유롭게 기록해보세요." />
              <div class="review-actions">
                <button @click="reviewText = ''">초기화</button>
                <button @click="saveDiary">기록 남기기</button>
              </div>
            </div>

            <!-- 회고 없음이고 오늘이 아님 -->
            <div v-else>
              <div class="readonly-diary empty">이 날은 회고를 남기지 않았어요 📝</div>
            </div>
          </div>

          <AddTaskModal
              v-if="showAddTaskModal"
              :categories="categories"
              :defaultDate="formatDate(selectedDate)"
              @close="showAddTaskModal = false"
              @submitted="loadTasksByDate"
          />
          <EditTaskModal
              v-if="showEditModal"
              :task="selectedTask"
              @close="closeEditModal"
              @update="handleTaskUpdate"
          />
          <!-- ✅ 커스텀 확인 모달 -->
          <ConfirmModal
              v-if="showConfirmModal"
              title="회고를 삭제할까요?"
              message="기존에 작성한 회고 내용은 모두 삭제됩니다."
              @close="showConfirmModal = false"
              @confirm="handleConfirmDelete"
          />

          <ConfirmModal
              v-if="showDeleteModal"
              title="정말 삭제할까요?"
              message="삭제하면 되돌릴 수 없습니다."
              @close="showDeleteModal = false"
              @confirm="handleConfirmDeleteTask"
          />
          <!-- 🔽 자동화 일정 모달 -->
          <AutomationScheduleModal
              v-if="showAutomationModal"
              :visible="showAutomationModal"
              @close="showAutomationModal = false"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import Header from '@/components/layout/Header.vue'
import { Calendar } from '@fullcalendar/core'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import {
  deleteTask,
  fetchCategory,
  fetchTasks,
  updateTask,
  updateTaskCompletion,
  fetchDiaryByDate,
  createDiary,
  updateDiary, deleteDiary
} from '@/features/main/mainApi'
import { useToast } from 'vue-toastification'
import ConfirmModal from "@/components/common/ConfirmModal.vue";

const toast = useToast()
import { useUserStore } from '@/stores/userStore';
import AddTaskModal from '@/features/main/components/AddTaskModal.vue'
import { showErrorToast } from '@/utill/toast.js';
import router from '@/router/index.js';
import { storeToRefs } from 'pinia';
import EditTaskModal from "@/features/main/components/EditTaskModal.vue";
import AutomationScheduleModal from "@/features/main/components/AutomationScheduleModal.vue";


const userStore = useUserStore();
const { userDeletedAt } = storeToRefs(userStore)
const reviewText = ref('')
const diary = ref(null)
const categories = ref([])
const calendarRef = ref(null)
const selectedDate = ref(new Date())
const selectedMonth = ref(new Date())
const showConfirmModal = ref(false)
const showAutomationModal = ref(false)


const showDeleteModal = ref(false)
const taskToDelete = ref(null)

const filteredCategories = computed(() =>
    categories.value.filter(
        (category) =>
            category &&
            Array.isArray(category.tasks) &&
            category.tasks.length > 0
    )
)
const formatDate = (date) => date.toISOString().split('T')[0]

const showAddTaskModal = ref(false)

const isEditing = ref(false)

const isToday = (createdAt) => {
  if (!createdAt) return false
  const today = new Date()
  const created = new Date(createdAt)
  return created.toDateString() === today.toDateString()
}

const editDiary = () => {
  isEditing.value = true
  reviewText.value = diary.value.diaryContent
}

const updateExistingDiary = async () => {
  try {
    await updateDiary(diary.value.diaryId, {
      diaryTitle: '회고',
      diaryContent: reviewText.value,
    })
    toast.success('회고 수정 완료!')
    isEditing.value = false
    await loadDiary()
  } catch (e) {
    toast.error('회고 수정 실패')
  }
}
const showEditModal = ref(false)
const selectedTask = ref(null)

const openEditTaskModal = (task) => {
  selectedTask.value = {
    ...task,
    scheduleDate: formatDate(selectedDate.value) // ✅ 여기가 중요
  }
  showEditModal.value = true
}
const closeEditModal = () => {
  showEditModal.value = false
  selectedTask.value = null
}

const confirmDeleteTask = (task) => {
  taskToDelete.value = task
  showDeleteModal.value = true
}

const handleTaskUpdate = async (updatedTask) => {
  try {
    const payload = {
      categoryId: updatedTask.categoryId,
      scheduleContent: updatedTask.scheduleContent,
      scheduleDate: updatedTask.scheduleDate
    }

    await updateTask(updatedTask.scheduleId, payload)

    toast.success("할 일이 성공적으로 수정되었습니다!")
    await loadTasksByDate()
    closeEditModal()
  } catch (error) {
    console.error("❌ 수정 오류:", error)
    // toast.error("할 일 수정 중 문제가 발생했어요.")
  }
}

// 회고 삭제 클릭 → 모달 띄우기
const deleteExistingDiary = () => {
  showConfirmModal.value = true
}

const isSameDate = (d1, d2) => {
  return new Date(d1).toDateString() === new Date(d2).toDateString()
}

const handleConfirmDeleteTask = async () => {
  try {
    await deleteTask(taskToDelete.value.scheduleId)
    toast.success("할 일이 삭제되었습니다!")
    await loadTasksByDate()
  } catch (e) {
    toast.error("삭제 중 오류가 발생했어요.")
  } finally {
    showDeleteModal.value = false
    taskToDelete.value = null
  }
}



const loadDiary = async () => {
  try {
    const dateStr = formatDate(selectedDate.value)
    const res = await fetchDiaryByDate(dateStr)
    diary.value = res.data.data
    reviewText.value = diary.value?.diaryContent || ''  // ✅ 이 줄 추가
  } catch (e) {
    diary.value = null
    reviewText.value = ''  // ✅ 실패한 경우도 초기화
  }
}

const saveDiary = async () => {
  try {
    const userStore = useUserStore()
    await createDiary({
      memberId: userStore.userId, // ✅ 필수
      diaryTitle: '회고',
      diaryContent: reviewText.value,
    })
    toast.success('회고가 저장되었습니다!')
    await loadDiary()
  } catch (e) {
    toast.error('회고 저장에 실패했어요.')
  }
}
const handleConfirmDelete = async () => {
  try {
    await deleteDiary(diary.value.diaryId)
    diary.value = null
    reviewText.value = ''
    toast.success('회고가 삭제되었습니다!')
  } catch (e) {
    toast.error('회고 삭제에 실패했어요.')
  } finally {
    showConfirmModal.value = false
  }
}

const isDarkColor = (hex) => {
  if (!hex) return false
  const color = hex.replace('#', '')
  const r = parseInt(color.substring(0, 2), 16)
  const g = parseInt(color.substring(2, 4), 16)
  const b = parseInt(color.substring(4, 6), 16)
  const brightness = (r * 299 + g * 587 + b * 114) / 1000
  return brightness < 128
}

const getTaskStyle = (color, completed) => {
  if (!completed) return {}
  return {
    backgroundColor: color,
    borderRadius: '8px',
    color: isDarkColor(color) ? 'white' : 'black'
  }
}

const handleUpdateTask = async (taskId, updatedData) => {
  try {
    await updateTask(taskId, updatedData)
    await fetchTasks()
  } catch (error) {
    console.error('수정 오류:', error)
  }
}

const handleDeleteTask = async (scheduleId) => {
  try {
    await deleteTask(scheduleId)
    await fetchTasks()
  } catch (error) {
    console.error('삭제 오류:', error)
  }
}

const loadTasksByDate = async () => {
  if (!selectedDate.value) return
  const scheduleDate = formatDate(selectedDate.value)

  for (const category of categories.value) {
    try {
      const res = await fetchTasks(category.categoryId, scheduleDate)
      const taskList = Array.isArray(res.data.data.schedule) ? res.data.data.schedule : []
      category.tasks = taskList.map(task => ({
        scheduleId: task.scheduleId,
        text: task.scheduleContent,
        completed: task.completionStatus,
        categoryId: category.categoryId // ✅ 여기!
      }))
    } catch (error) {
      console.error(`❌ 카테고리 ${category.title}의 할 일 조회 실패`, error)
      category.tasks = []
    }
  }
}

const toggleTaskCompletion = async (task) => {
  try {
    await updateTaskCompletion(task.scheduleId, task.completed)
    console.log(`✅ ${task.text} 상태 업데이트 완료`)
  } catch (e) {
    console.error('❌ 상태 업데이트 실패', e)
    task.completed = !task.completed
  }
}

watch(selectedDate, async () => {
  await loadTasksByDate()
  await loadDiary()
  console.log('📘 diary:', diary.value)
}, { immediate: true })

onMounted(async () => {
  if (userDeletedAt.value) {
    showErrorToast('이미 탈퇴한 회원입니다. 로그아웃 후 메인 화면으로 이동합니다.');
    userStore.logout();
    await router.push('/');
    return;
  }

  const response = await fetchCategory()
  categories.value = response.data.data.map(category => ({
    categoryId: category.categoryId,
    title: category.categoryName,
    color: category.color,
    completed: category.completed,
    tasks: []
  }))

  const calendar = new Calendar(calendarRef.value, {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'ko',
    height: 'auto',
    dateClick: (info) => {
      selectedDate.value = new Date(info.dateStr)
      document.querySelectorAll('.fc-daygrid-day').forEach(cell => cell.classList.remove('selected-date'))
      info.dayEl.classList.add('selected-date')
    },
    datesSet: (info) => {
      const currentMonth = info.view.currentStart
      selectedMonth.value = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1)
    }
  })

  calendar.render()
  selectedMonth.value = calendar.getDate()
  await loadTasksByDate()
  await loadDiary()
})
</script>




<style scoped>
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #F8F7FF;
  padding: 40px 0;
  min-height: calc(100vh - 80px);
}
.main {
  width: 1200px;
  display: flex;
  gap: 32px;
}
.calendar-content {
  width: 480px;
  height: 560px;
  background: #FFFFFF;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
}
.calendar-box {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
#calendar {
  width: 100%;
  height: 100%;
}
.right-content {
  width: 648px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 600px;
  overflow-y: auto;
  box-sizing: border-box;
  padding: 0;
}
.today-task-box,
.review-box {
  background: #FFFFFF;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  flex-shrink: 0;
}
.today-task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.today-task-header h4 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}
.add-task-btn {
  font-size: 14px;
  padding: 6px 12px;
  background-color: #9381FF;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.button-group {
  display: flex;
  gap: 10px;
}

.category {
  margin-bottom: 16px;
  border-radius: 12px;
  padding: 16px;
  background-color: #F8F7FF;
}
.category-title {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 12px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 10px;
}
.category-tag {
  display: inline-block;
  padding: 12px 2px;
  width: 8px;
  height: 8px;
  font-size: 12px;
  border-radius: 15px;
  color: #333;
}
.category-task {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #ccc;
}
.readonly-diary {
  white-space: pre-line;
  background-color: #f4f4f4;
  padding: 16px;
  border-radius: 8px;
  color: #333;
  font-size: 15px;
}

.task-text {
  flex-grow: 1;
  margin-left: 10px;
  color: #333;
}
.task-actions {
  display: flex;
  gap: 8px;
}
.task-actions {
  display: flex;
  gap: 8px;
}

.btn {
  font-size: 14px;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
  color: #fff;
}

.edit-btn {
  background-color: #4D96FF;
}
.edit-btn:hover {
  background-color: #2F6FE4;
}

.delete-btn {
  background-color: #FF6B6B;
}
.delete-btn:hover {
  background-color: #E04848;
}

.review-box textarea {
  width: 100%;
  height: 120px;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 10px;
  resize: none;
  margin-top: 12px;
}
.review-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}
.review-actions button {
  padding: 8px 16px;
  font-size: 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.review-actions button:first-child {
  background-color: #FFEEDD;
  color: #333;
}
.review-actions button:last-child {
  background-color: #FFD8BE;
  color: #333;
}

.automation-btn {
  font-size: 14px;
  padding: 6px 12px;
  margin-left: 10px;
  background-color: #ddddff;
  color: #333;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
:deep(.selected-date) {
  background-color: #4D96FF !important;
  color: white !important;
  border-radius: 6px;
}
</style>
