export const categoryRoutes = [
    {
        path: '/category',
        name: 'category',
        component: () => import('@/features/category/views/CategoryView.vue'),
        meta: { requiresGuest: true }
    }
]