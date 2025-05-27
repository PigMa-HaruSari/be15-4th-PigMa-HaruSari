export const feedbackRoutes = [
    {
        path: '/feedback',
        name: 'feedback',
        component: () => import('@/features/feedback/views/FeedbackView.vue'),
        meta: { requiresAuth: true }
    }
]