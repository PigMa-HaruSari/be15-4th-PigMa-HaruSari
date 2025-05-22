<script setup>
import {ref, onMounted, watch, nextTick, computed} from 'vue'
import axios from 'axios'
import {Chart} from 'chart.js'
import {format} from 'date-fns'
import api from "@/lib/api.js";

const props = defineProps({date: Date})
const chartRef = ref(null)
let chartInstance = null

const drawChart = (rate) => {
  if (!chartRef.value) return
  if (chartInstance) chartInstance.destroy()
  chartInstance = new Chart(chartRef.value, {
    type: 'doughnut',
    data: {
      labels: [' 완료', ' 미완료'],
      datasets: [{
        data: [rate, 100 - rate],
        backgroundColor: ['#a2d2ff', '#ffc8dd'] // 또는 네가 쓰는 색
      }]
    },
    options: {
      responsive: false,
      plugins: {
        legend: {
          display: true,
          position: 'bottom',
          labels: {
            usePointStyle: true,
            boxWidth: 12,
            padding: 16
          }
        },
        tooltip: {
          callbacks: {
            label: function (context) {
              const value = context.parsed
              const label = context.label
              return `${label} : ${value}%`
            }
          }
        }
      },
      cutout: '60%' // 도넛 두께
    }
  })
}

const loadData = async () => {
  const dateStr = format(props.date, 'yyyy-MM-dd')
  // const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/statistics/daily`, {params: {date: dateStr}})
  const res = await api.get(`${import.meta.env.VITE_API_BASE_URL}/statistics/daily`, {params: {date: dateStr}})
  const rate = res.data.data.achievementRate
  await nextTick()
  drawChart(rate)
}

const labelText = computed(() => `${props.date.getDate()}일 달성률`)

watch(() => props.date, loadData, {immediate: true})
</script>

<template>
  <div class="circle-box">
    <h4>{{ labelText }}</h4>
    <canvas ref="chartRef" width="150" height="150"></canvas>
  </div>
</template>

<style scoped>
.circle-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: 100%;
  padding: 16px 0;
}

h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}

canvas {
  display: block;
  margin-bottom: 20px;
}
</style>