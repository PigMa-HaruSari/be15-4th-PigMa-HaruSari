<script setup>
import {ref, onMounted, nextTick} from 'vue'
import {Chart} from 'chart.js/auto'
import api from "@/lib/axios.js";
import {fetchCategoryStatistics} from "@/features/statistics/statisticsApi.js";

const chartRef = ref(null)
let chartInstance = null

const labels = ref([])
const rates = ref([])
const colors = ref([])

const drawChart = () => {
  if (!chartRef.value) return
  if (chartInstance) chartInstance.destroy()
  chartInstance = new Chart(chartRef.value, {
    type: 'bar',
    data: {
      labels: labels.value,
      datasets: [{
        label: '달성률 (%)',
        data: rates.value,
        backgroundColor: colors.value,
        borderRadius: 6,
        categoryPercentage: 0.5,
        barPercentage: 0.8
      }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      layout: {
        padding: 12
      },
      scales: {
        x: {
          min: 0,
          max: 100,
          ticks: {
            callback: value => `${value}%`,
            color: '#444'
          },
          grid: {
            drawBorder: false
          }
        },
        y: {
          ticks: {
            color: '#222'
          },
          grid: {
            display: false
          }
        }
      },
      plugins: {
        legend: {
          position: 'top',
          labels: {
            boxWidth: 12,
            padding: 16
          }
        },
        tooltip: {
          callbacks: {
            label: ctx => `${ctx.raw}%`
          }
        }
      }
    }
  })
}

const loadData = async () => {
  try {
    const res = await fetchCategoryStatistics()
    const list = res.data?.data?.categoryRates || []
    labels.value = list.map(item => item.categoryName)
    rates.value = list.map(item => item.achievementRate)
    colors.value = list.map(item => item.color)
    await nextTick()
    drawChart()
  } catch (e) {
    console.error('[ERROR] 카테고리 통계 조회 실패:', e)
  }
}

onMounted(loadData)
</script>

<template>
  <div>
    <h4>카테고리별 통계</h4>
    <canvas ref="chartRef" height="200"></canvas>
  </div>
</template>

<style scoped>
h4 {
  margin-bottom: 16px;
  font-weight: bold;
  font-size: 18px;
}
</style>