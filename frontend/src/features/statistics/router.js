export const statisticsRoutes = [
    {
        path: '/statistics',
        name: 'statistics',
        component: () => import('@/features/statistics/views/StatisticsView.vue'),
        meta: { requiresAuth: true }
    }
]