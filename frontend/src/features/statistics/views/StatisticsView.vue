<script setup>
import {ref, onMounted} from 'vue'
import {Calendar} from '@fullcalendar/core'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

import DailyStatistics from '../components/DailyStatistics.vue'
import MonthlyStatistics from '../components/MonthlyStatistics.vue'
import CategoryStatistics from '../components/CategoryStatistics.vue'

const calendarRef = ref(null)
const selectedDate = ref(new Date())
const selectedMonth = ref(new Date())

onMounted(() => {
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

<template>
  <div class="statistics-wrapper">
    <div class="left-column">
      <div id="calendar" ref="calendarRef"></div>
    </div>
    <div class="right-column">
      <div class="donut-section">
        <div class="donut-box">
          <DailyStatistics :date="selectedDate"/>
        </div>
        <div class="donut-box">
          <MonthlyStatistics :date="selectedMonth"/>
        </div>
      </div>
      <div class="bar-section">
        <CategoryStatistics/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.statistics-wrapper {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  gap: 40px;
  align-items: flex-start;
}

.left-column {
  width: 520px;
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.right-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.donut-section {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.donut-box {
  flex: 1;
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.bar-section {
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

:deep(.selected-date) {
  background-color: #4D96FF !important;
  color: white !important;
  border-radius: 6px;
}
</style>