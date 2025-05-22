/* user 관련 api 호출 */
import axios from "axios";



export function refreshUserToken() {
    const refreshToken = localStorage.getItem('refreshToken');

    return axios.post(
        `${import.meta.env.VITE_API_BASE_URL}/auth/refresh`,
        { refreshToken }, // body
        {
            headers: {
                Authorization: `Bearer ${refreshToken}` // 헤더
            },
            withCredentials: true
        }
    );
}