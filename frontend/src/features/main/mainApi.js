import api from "@/lib/api.js";

export const fetchCategory = () => {
    return api.get("/categories"); // memberId 제거
};


export default {
    fetchCategory
};