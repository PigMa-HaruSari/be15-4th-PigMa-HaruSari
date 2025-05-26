import api from "@/lib/axios.js";

export const fetchCategory = () => {
    return api.get("/categories"); // memberId 제거
};
/**
 * 특정 날짜와 카테고리 기준으로 할 일 목록을 조회합니다.
 *
 * @param {number} categoryId - 카테고리 ID
 * @param {string} date - YYYY-MM-DD 형식의 날짜 문자열
 * @returns AxiosResponse
 */
export const fetchTasks = (categoryId, date) => {
    return api.get("/task/schedule", {
        params: {
            categoryId,
            scheduleDate: date,
        },
    });
};
export const createTask = (data) => {
    return api.post('/task/schedule', data)
}

export const updateTask = (scheduleId, data) => {
    return api.put(`/task/schedule/${scheduleId}`, data)
}


export const deleteTask = (scheduleId) => {
    return api.delete(`/task/schedule/${scheduleId}`);
};

export const updateTaskCompletion = (scheduleId, completionStatus) => {
    return api.put(`/task/schedule/${scheduleId}/completionstatus`, { completionStatus });
};

export const fetchDiaryByDate = async (date) => {
    return await api.get(`/diary`, { params: { date } });
};

export const createDiary = async (payload) => {
    return await api.post(`/diary`, payload);
};

export const updateDiary = async (diaryId, payload) => {
    return await api.put(`/diary/${diaryId}`, payload)
}

export const deleteDiary = async (diaryId) => {
    return await api.delete(`/diary/${diaryId}`)
}




export default {
    fetchCategory,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    fetchDiaryByDate,
    createDiary,
    updateDiary,
    deleteDiary
};