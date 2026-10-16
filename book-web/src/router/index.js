import { createRouter, createWebHistory } from 'vue-router'
import request from '../utils/request' // 导入你的axios实例

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/book-manage',
    name: 'BookManage',
    component: () => import('../views/BookManage.vue')
  },
  {
    path: '/borrowRecord',
    name: 'BorrowRecord',
    component: () => import('../views/BorrowRecord.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue')
  }


]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Session模式路由守卫
router.beforeEach((to, from, next) => {
  const hasLogin = localStorage.getItem("hasLogin")
  // 白名单：登录、注册页面，不需要登录也可以访问
  const whiteList = ['/login', '/register']
  if (!whiteList.includes(to.path) && !hasLogin) {
    next('/login')
  } else {
    next()
  }
})






export default router
