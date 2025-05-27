// src/features/feedback/feedbackApi.js
import api from '@/lib/axios.js'

// 피드백 요약 리스트
export const fetchFeedbackList = async () => {
    return api.get('/feedbacks')
}

// 피드백 상세
export const fetchFeedbackDetail = async (feedbackId) => {
    return api.get(`/feedbacks/${feedbackId}`)
}


export default {
    fetchFeedbackList,
    fetchFeedbackDetail
};