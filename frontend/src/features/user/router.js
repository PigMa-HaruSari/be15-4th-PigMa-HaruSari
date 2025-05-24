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
    {
        path: '/signup/register',
        name: 'SignupRegister',
        component: () => import('@/features/user/views/SignupRegisterView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/signup/kakao/callback',
        name: 'KakaoRedirect',
        component: () => import('@/features/user/views/KakaoRedirectView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/login/kakao/callback',
        name: 'KakaoLoginRedirect',
        component: () => import('@/features/user/views/KakaoLoginRedirectView.vue'),
        meta: { requiresGuest: true }
    },
    {
        path: '/mypage',
        name: 'MyPage',
        component: () => import('@/features/user/views/MyPageView.vue'),
        meta: { requiresAuth: true }
    }


]