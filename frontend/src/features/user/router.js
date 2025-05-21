export const loginRoutes = [
    {
        path: '/login',
        name: 'login',
        component: () => import('@/features/user/views/LoginView.vue')
    }
]