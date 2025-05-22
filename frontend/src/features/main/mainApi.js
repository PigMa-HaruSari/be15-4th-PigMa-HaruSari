import api from "@/lib/axios.js";

export const fetchCategory = () => {
    return api.get("/categories"); // memberId 제거
};


export default {
    fetchCategory
};