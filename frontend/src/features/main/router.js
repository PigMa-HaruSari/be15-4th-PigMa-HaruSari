export const mainRoutes = [
    {
        path: '/',
        name: 'main',
        component: () => import('@/features/main/views/MainPageView.vue'),
        meta: { requiresAuth: true }
    }
]