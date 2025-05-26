<template>
  <div class="dropdown-wrapper" ref="dropdownRef">
    <button @click="toggleDropdown" class="notification-icon">
      <img src="@/assets/images/header-bell-white.svg" alt="알림" />
      <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
    </button>
    <div v-if="open" class="dropdown-panel">
      <div v-if="notifications.length === 0" class="empty">📭 알림이 없습니다</div>
      <ul v-else class="notification-list">
        <li
            v-for="(noti, index) in notifications"
            :key="index"
            class="notification-item"
            :class="{ unread: !noti.isRead }"
        >
          <div class="message">{{ noti.alarmMessage }}</div>
          <div class="date">{{ formatDate(noti.createdAt) }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { fetchAlarmList, markAllAlarmsAsRead } from '@/features/alarm/alarmApi.js'

const open = ref(false)
const notifications = ref([])
const dropdownRef = ref(null)

const toggleDropdown = () => {
  open.value = !open.value
}

watch(open, async (isOpen) => {
  if (isOpen) {
    try {
      const res = await fetchAlarmList()
      notifications.value = res.data.data
    } catch (e) {
      console.error('알림 불러오기 실패:', e)
    }
  } else {
    try {
      await markAllAlarmsAsRead()
      notifications.value.forEach(n => n.isRead = true)
    } catch (e) {
      console.error('알림 읽음 처리 실패:', e)
    }
  }
})

const unreadCount = computed(() =>
    notifications.value.filter(n => n.isRead === false).length
)

const handleClickOutside = (e) => {
  if (dropdownRef.value && !dropdownRef.value.contains(e.target)) {
    open.value = false
  }
}

onMounted(async () => {
  try {
    const res = await fetchAlarmList()
    notifications.value = res.data.data
  } catch (e) {
    console.error('초기 알림 불러오기 실패:', e)
  }
  window.addEventListener('click', handleClickOutside)
})
onBeforeUnmount(() => {
  window.removeEventListener('click', handleClickOutside)
})

function formatDate(dateStr) {
  const d = new Date(dateStr)
  return isNaN(d.getTime()) ? '날짜 없음' : d.toLocaleString('ko-KR', { hour12: false })
}
</script>

<style scoped>
.dropdown-wrapper {
  position: relative;
}
.dropdown-panel {
  position: absolute;
  top: 140%;
  right: 0;
  background: #fff;
  color: #333;
  width: 320px;
  max-height: 400px;
  overflow-y: auto;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  z-index: 9999;
  padding: 1rem;
  font-size: 0.95rem;
}
.notification-list {
  list-style: none;
  margin: 0;
  padding: 0;
}
.notification-item {
  padding: 0.75rem 0;
  border-bottom: 1px solid #eee;
}
.notification-item.unread {
  background-color: #f8f8ff;
  font-weight: 600;
}
.message {
  font-weight: 500;
  color: #222;
}
.date {
  font-size: 0.8rem;
  color: #888;
  margin-top: 0.25rem;
}
.empty {
  text-align: center;
  color: #aaa;
  padding: 1.5rem 0;
}
.notification-icon {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  position: relative;
}
.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 2px 6px;
  font-size: 0.7rem;
}
</style>