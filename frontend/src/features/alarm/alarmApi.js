// 📁 src/features/alarm/alarmApi.js
import api from '@/lib/axios.js'

// 알림 목록 조회 API
export const fetchAlarmList = () => {
    return api.get('/alarms/unread')
}
export const markAllAlarmsAsRead = () => {
    return api.put('/alarms/read-all')
}

export default {
    fetchAlarmList,
    markAllAlarmsAsRead
}