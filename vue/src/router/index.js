import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import login from "../views/login";
import layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'layout',
    component: layout,
    redirect:"/home",
    children:[
      {path: 'home',
        name: 'home',
        component: Home,
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
