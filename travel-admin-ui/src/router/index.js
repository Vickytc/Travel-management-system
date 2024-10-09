import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/home',
      component: () => import('../components/LayOut.vue'),
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('../views/home/HomeView.vue')
        },
        {
          path: '/user_admin',
          name: 'UserAdmin',
          component: () => import('../views/user/UserAdminView.vue')
        },
        {
          path: '/hotel_admin',
          name: 'HotelAdmin',
          component: () => import('../views/hotel/HotelAdminView.vue')
        },
        {
          path: '/room_admin/:hotelId',
          name: 'RoomAdmin',
          component: () => import('../views/room/RoomAdminView.vue')
        },
        {
          path: '/reserve_admin',
          name: 'ReserveAdmin',
          component: () => import('../views/reserve/ReserveAdminView.vue')
        },
        {
          path: '/comment_admin',
          name: 'CommentAdmin',
          component: () => import('../views/comment/CommentAdminView.vue')
        },
        {
          path: '/hotel',
          name: 'Hotel',
          component: () => import('../views/hotel/HotelView.vue')
        },
      ]
    },
  ]
})

export default router
