import axios from 'axios'

export const getUnreadAlarms = () => {
    return axios.get('http://localhost:8080/api/v1/alarms/unread')
}

export const markAllAsRead = () => {
    return axios.put('http://localhost:8080/api/v1/alarms/read-all')
}
