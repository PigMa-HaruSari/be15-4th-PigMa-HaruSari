export const userRoutes = [
    {
        path: '/signup',
        name: 'signup',
        component: () => import('@/features/user/views/SignupView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/features/user/views/LoginView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/signup',
        name: 'Signup',
        component: () => import('@/features/user/views/SignupView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/signup/info',
        name: 'SignupInfo',
        component: () => import('@/features/user/views/SignupInfoView.vue'),
        meta: { requiresGuest: true }
    },
]