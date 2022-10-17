import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/login'
import Register from '@/components/register'
import home from '../components/home'
import scene_router from "@/router/scene_router";
import user_router from "@/router/user_router";
import store from '../store'

Vue.use(Router)

export const router = new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: home,
            meta: {
                requireAuth: true // 需要登录权限
            },
            children: [
                ...scene_router,
                ...user_router,
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: Login
        },
        {
            path: '/register',
            name: 'Register',
            component: Register
        },
        {
            path: '*',
            redirect: '/select_scene'
        }
    ]
})

//前端登录拦截
router.beforeEach(function (to, from, next) {
  if (to.matched.some(record => record.meta.requireAuth)) {
    if (store.state.token) {
      next()
    } else {
      next({
        path: '/login',
        query: {redirect: to.fullPath} // 登录成功之后重新跳转到该路由
      })
    }
  } else {
    next()
  }
})
