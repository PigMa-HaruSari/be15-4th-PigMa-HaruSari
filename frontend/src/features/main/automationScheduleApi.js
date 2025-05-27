import api from "@/lib/axios.js";


// 자동화 일정 전체 조회
export const fetchAutomationSchedules = () => {
    return api.get('/task/automationschedules')
}

// 자동화 일정 등록
export const createAutomationSchedule = (data) => {
    return api.post('/task/automationschedules', data)
}

// 자동화 일정 수정
export const updateAutomationSchedule = (id, data) => {
    return api.put(`/task/automationschedules/${id}`, data)
}

// 자동화 일정 삭제
export const deleteAutomationSchedule = (id) => {
    return api.delete(`/task/automationschedules/${id}`)
}


export default {
    fetchAutomationSchedules,
    createAutomationSchedule,
    updateAutomationSchedule,
    deleteAutomationSchedule
};