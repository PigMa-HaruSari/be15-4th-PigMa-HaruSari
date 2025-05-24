import api from '@/lib/axios.js'

// 카테고리별 통계
export const fetchCategoryStatistics = () => {
    return api.get('/statistics/category')
}

// 일일 통계
export const fetchDailyStatistics = (date) => {
    return api.get('/statistics/daily', { params: { date } })
}

// 월간 통계
export const fetchMonthlyStatistics = (date) => {
    return api.get('/statistics/monthly', { params: { date } })
}

export default{
    fetchDailyStatistics,
    fetchMonthlyStatistics,
    fetchCategoryStatistics
}