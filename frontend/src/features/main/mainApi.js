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
    return api.post('/schedule', data)
}

export default {
    fetchCategory,
    fetchTasks,
    createTask
};