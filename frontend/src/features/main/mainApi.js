import api from "@/lib/api.js";

export const fetchCategory = () => {
    return api.get('/categories?memberId=${memberId}', {
        params: {
        },
    });
};


export default {
    fetchCategory
};