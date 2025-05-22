export const userRoutes = [
    {
        path: '/signup',
        name: 'signup',
        component: () => import('@/features/user/views/SignupView.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/features/user/views/LoginView.vue'),
    },
    {
        path: '/signup',
        name: 'Signup',
        component: () => import('@/features/user/views/SignupView.vue'),
    },

]