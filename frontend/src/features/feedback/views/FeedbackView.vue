<template>
  <Header/>
  <div class="feedback-container">
    <h1 class="title">📘 피드백 목록</h1>

    <!-- 에러 메시지 -->
    <div v-if="errorMessage" class="error-box">
      ⚠️ {{ errorMessage }}
    </div>

    <!-- 로딩 -->
    <div v-if="loading" class="loading">불러오는 중...</div>

    <!-- 피드백 없음 -->
    <div v-else-if="feedbackList.length === 0" class="empty">
      📭 등록된 피드백이 없습니다.
    </div>

    <!-- 피드백 리스트 -->
    <div v-else class="feedback-list">
      <div
          v-for="item in feedbackList"
          :key="item.feedbackId"
          class="feedback-card"
          :class="{ active: selectedId === item.feedbackId }"
          @click="selectFeedback(item.feedbackId)"
      >
        <div class="feedback-date">🗓 {{ item.createdAt }}</div>
        <div class="feedback-summary">{{ item.summary }}</div>

        <!-- 상세 피드백 -->
        <div v-if="selectedId === item.feedbackId && feedbackDetail" class="feedback-detail">
          <p class="feedback-detail-date">🕒 {{ feedbackDetail.createdAt }}</p>
          <Markdown class="markdown-body" :source="feedbackDetail.content" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchFeedbackList, fetchFeedbackDetail } from '@/features/feedback/feedbackApi.js'
import Markdown from 'vue3-markdown-it'
import Header from "@/components/layout/Header.vue";

const feedbackList = ref([])
const feedbackDetail = ref(null)
const selectedId = ref(null)
const loading = ref(true)
const errorMessage = ref(null)

onMounted(async () => {
  try {
    const res = await fetchFeedbackList()
    feedbackList.value = res.data.data
  } catch (e) {
    if (e.response?.status === 401) {
      errorMessage.value = '로그인이 필요합니다. 먼저 로그인해주세요.'
    } else {
      errorMessage.value = '피드백 목록을 불러오는 데 실패했습니다.'
    }
  } finally {
    loading.value = false
  }
})

const selectFeedback = async (id) => {
  // 이미 선택된 항목이면 접기
  if (selectedId.value === id) {
    selectedId.value = null
    feedbackDetail.value = null
    return
  }

  try {
    const res = await fetchFeedbackDetail(id)
    const data = res.data.data

    // 마크다운 텍스트만 추출
    if (typeof data.content === 'string' && data.content.startsWith('{text=')) {
      data.content = data.content.replace(/^\{text=/, '').replace(/\}$/, '').trim()
    }

    feedbackDetail.value = data
    selectedId.value = id
  } catch (e) {
    errorMessage.value = '피드백 상세 조회에 실패했습니다.'
  }
}
</script>

<style scoped>
.feedback-container {
  padding: 2rem;
  max-width: 960px;
  margin: 80px auto 0;  /* 👈 상단 여백 추가 */
}

.title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
}

.error-box {
  color: #b00020;
  background: #ffebee;
  padding: 1rem;
  border-radius: 6px;
  margin-bottom: 1rem;
}

.loading {
  font-size: 1.2rem;
  color: gray;
}

.empty {
  font-size: 1.1rem;
  color: #777;
}

.feedback-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feedback-card {
  background: #ffffff;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 1.2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: box-shadow 0.2s ease;
}

.feedback-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.feedback-card.active {
  border-color: #4285f4;
  background: #f8faff;
}

.feedback-date {
  font-size: 0.85rem;
  color: #777;
  margin-bottom: 0.3rem;
}

.feedback-summary {
  font-weight: 600;
  font-size: 1rem;
  color: #333;
}

.feedback-detail {
  margin-top: 1rem;
  border-top: 1px dashed #ccc;
  padding-top: 1rem;
}

.feedback-detail-date {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.markdown-body {
  font-size: 1rem;
  line-height: 1.7;
  white-space: pre-wrap;
}

.markdown-body h2 {
  font-size: 1.2rem;
  margin-top: 1.2rem;
}

.markdown-body ul {
  padding-left: 1.5rem;
  list-style-type: disc;
}
</style>
