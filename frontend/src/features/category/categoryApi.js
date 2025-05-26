import api from "@/lib/axios.js";


// 카테고리 생성 요청
export const createCategory = async (categoryData) => {
    return api.post("/categories/create", categoryData)
}

// 수정
export const updateCategory = (categoryId, data) => {
    return api.put(`/categories/${categoryId}`, data);
};

// 완료 처리
export const completeCategory = (categoryId) => {
    return api.put(`/categories/${categoryId}/complete`);
};

// 삭제
export const deleteCategory = (categoryId, confirmText = "카테고리를 삭제하겠습니다", hasSchedules = false) => {
    return api.delete(`/categories/${categoryId}`, {
        params: {
            confirmText,
            hasSchedules
        }
    });
};

export default {
    createCategory,
    updateCategory,
    completeCategory,
    deleteCategory
};
