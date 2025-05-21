import { ref } from 'vue'
import { getUnreadAlarms, markAllAsRead } from '../api.js'

export function useAlarm() {
    const alarms = ref([])
    const isLoading = ref(false)
    const error = ref(null)

    const loadAlarms = async () => {
        isLoading.value = true
        error.value = null
        try {
            const res = await getUnreadAlarms()
            alarms.value = res.data.data
        } catch (e) {
            error.value = 'Failed to load alarms'
        } finally {
            isLoading.value = false
        }
    }

    const readAll = async () => {
        await markAllAsRead()
    }

    return {
        alarms,
        isLoading,
        error,
        loadAlarms,
        readAll,
    }
}
