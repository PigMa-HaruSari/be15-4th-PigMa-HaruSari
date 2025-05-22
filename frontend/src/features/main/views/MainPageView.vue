<!--
<template>
  <div>
    <Header />
    <div class="main-wrapper">
      <div class="main">
        &lt;!&ndash; 달력 영역 &ndash;&gt;
        <div class="calendar-content">
          <div class="calendar-box">
&lt;!&ndash;            <div id="calendar"></div>&ndash;&gt;
            <div id="calendar" ref="calendarRef"></div>
          </div>
        </div>

        &lt;!&ndash; 할 일 + 회고 영역 &ndash;&gt;
        <div class="right-content">
          <div class="today-task-box">
            <div class="today-task-header">
              <h4>오늘 할 일</h4>
              <button class="add-task-btn">할 일 추가</button>
            </div>

            &lt;!&ndash; 카테고리별 할 일 리스트 &ndash;&gt;
            <div class="category" v-for="(category, index) in categories" :key="index">
              <div class="category-title">
                <span class="category-tag" :style="{ backgroundColor: category.color }"></span>
                {{ category.title }}
              </div>
              <div
                  class="category-task"
                  v-for="(task, i) in category.tasks"
                  :key="i"
                  :style="task.completed ? { backgroundColor: category.color, borderRadius: '8px' } : {}"
              >
                <input type="checkbox" v-model="task.completed" />
                {{ task.text }}
              </div>
            </div>
          </div>

          &lt;!&ndash; 회고 영역 &ndash;&gt;
          <div class="review-box">
            <h4>회고</h4>
            <textarea v-model="reviewText" placeholder="오늘의 회고를 작성해보세요..."></textarea>
            <div class="review-actions">
              <button @click="reviewText = ''">회고 삭제</button>
              <button @click="saveReview">회고 저장</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Header from '@/components/layout/Header.vue';
import { Calendar } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import koLocale from '@fullcalendar/core/locales/ko';
import { fetchCategory } from '@/features/main/mainApi';
import interactionPlugin from "@fullcalendar/interaction";

const reviewText = ref('');
const categories = ref([]);
const calendarRef = ref(null)
const selectedDate = ref(new Date())
const selectedMonth = ref(new Date())

function saveReview() {
  alert('회고가 저장되었습니다.');
}

onMounted(async () => {
  const response = await fetchCategory();

  categories.value = response.data.data.map(category => ({
    title: category.categoryName,
    color: category.color,
    tasks: [] // 추후 할 일 연동 가능
  }));

/*  const calendarEl = document.getElementById('calendar');
  const calendar = new Calendar(calendarEl, {
    plugins: [dayGridPlugin],
    initialView: 'dayGridMonth',
    height: '100%',
    locale: koLocale,
    timeZone: 'local'
  });
  calendar.render();*/
  const calendar = new Calendar(calendarRef.value, {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'ko',
    height: 'auto',
    dateClick: (info) => {
      selectedDate.value = new Date(info.dateStr)

      const allCells = document.querySelectorAll('.fc-daygrid-day')
      allCells.forEach(cell => cell.classList.remove('selected-date'))

      const clickedCell = info.dayEl
      clickedCell.classList.add('selected-date')
    }
    ,
    datesSet: (info) => {
      // 현재 표시 중인 달의 첫 번째 날짜 기준으로 설정
      const currentMonth = info.view.currentStart
      selectedMonth.value = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1)
    }
  })

  calendar.render()

  const initialMonthDate = calendar.getDate()
  selectedMonth.value = new Date(initialMonthDate.getFullYear(), initialMonthDate.getMonth(), 1)
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
  font-size: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #ccc;
}
.category-task:last-child {
  border-bottom: none;
}
.category-task input[type='checkbox'] {
  margin-right: 10px;
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
:deep(.selected-date) {
  background-color: #4D96FF !important;
  color: white !important;
  border-radius: 6px;
}
</style>
-->
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
              <button class="add-task-btn">할 일 추가</button>
            </div>

            <!-- 카테고리별 할 일 리스트 -->
            <div class="category" v-for="(category, index) in categories" :key="index">
              <div class="category-title">
                <span class="category-tag" :style="{ backgroundColor: category.color }"></span>
                {{ category.title }}
              </div>
              <div
                  class="category-task"
                  v-for="(task, i) in category.tasks"
                  :key="i"
                  :style="task.completed ? { backgroundColor: category.color, borderRadius: '8px' } : {}"
              >
                <input type="checkbox" v-model="task.completed" />
                {{ task.text }}
              </div>
            </div>
          </div>

          <!-- 회고 영역 -->
          <div class="review-box">
            <h4>회고</h4>
            <textarea v-model="reviewText" placeholder="오늘의 회고를 작성해보세요..."></textarea>
            <div class="review-actions">
              <button @click="reviewText = ''">회고 삭제</button>
              <button @click="saveReview">회고 저장</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import Header from '@/components/layout/Header.vue';
import { Calendar } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import koLocale from '@fullcalendar/core/locales/ko';
import interactionPlugin from '@fullcalendar/interaction';
import {fetchCategory, fetchTasks} from '@/features/main/mainApi';

const reviewText = ref('');
const categories = ref([]);
const calendarRef = ref(null);
const selectedDate = ref(new Date());
const selectedMonth = ref(new Date());

function saveReview() {
  alert('회고가 저장되었습니다.');
}

const formatDate = (date) => {
  return date.toISOString().split('T')[0];
};

const loadTasksByDate = async (date) => {
  const formatted = formatDate(date);
  for (const category of categories.value) {
    try {
      const res = await fetchTasks(category.categoryId, formatted);
      category.tasks = res.data.data.map(task => ({
        text: task.title,
        completed: task.completed
      }));
    } catch (err) {
      console.error(`카테고리 ${category.title}의 할 일 조회 실패`, err);
      category.tasks = [];
    }
  }
};

watch(selectedDate, async (newDate) => {
  await loadTasksByDate(newDate);
});

onMounted(async () => {
  const response = await fetchCategory();

  categories.value = response.data.data.map(category => ({
    categoryId: category.categoryId,
    title: category.categoryName,
    color: category.color,
    tasks: []
  }));

  const calendar = new Calendar(calendarRef.value, {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'ko',
    height: 'auto',
    dateClick: (info) => {
      selectedDate.value = new Date(info.dateStr);
      const allCells = document.querySelectorAll('.fc-daygrid-day');
      allCells.forEach(cell => cell.classList.remove('selected-date'));
      const clickedCell = info.dayEl;
      clickedCell.classList.add('selected-date');
    },
    datesSet: (info) => {
      const currentMonth = info.view.currentStart;
      selectedMonth.value = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1);
    }
  });

  calendar.render();

  const initialMonthDate = calendar.getDate();
  selectedMonth.value = new Date(initialMonthDate.getFullYear(), initialMonthDate.getMonth(), 1);

  await loadTasksByDate(selectedDate.value);
});
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
  font-size: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #ccc;
}
.category-task:last-child {
  border-bottom: none;
}
.category-task input[type='checkbox'] {
  margin-right: 10px;
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
:deep(.selected-date) {
  background-color: #4D96FF !important;
  color: white !important;
  border-radius: 6px;
}
</style>