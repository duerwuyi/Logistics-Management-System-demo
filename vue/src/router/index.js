import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import login from "../views/login";
import error from "../views/404";
import layout from "../layout/Layout";
import Register from "../views/Register";
import userspace from "../views/userspace";
import orderlist from "@/views/orderlist";
import Car from "@/views/Car";
import Driver from "@/views/Driver";
import orderfordriver from "@/views/orderfordriver";
import orderforuser from "@/views/orderforuser";

const routes = [
  {
    path: '/',
    name: 'layout',
    component: layout,
    redirect:"/userspace",
    children:[
      {path: 'home',
        name: 'home',
        component: Home,
        meta: {
          roles: ['admin']
        },
      },
      {path: 'userspace',
        name: 'userspace',
        component: userspace,
        meta: {
          roles: ['admin', 'user','employee']
        },
      },
      {path: 'orderlist',
        name: 'orderlist',
        component: orderlist,
        meta: {
          roles: ['admin']
        },
      },
      {path: 'car',
        name: 'Car',
        component: Car,
        meta: {
          roles: ['admin']
        },
      },
      {path: 'driver',
        name: 'Driver',
        component: Driver,
        meta: {
          roles: ['admin']
        },
      },
      {path: 'orderfordriver',
        name: 'orderfordriver',
        component: orderfordriver,
        meta: {
          roles: ['employee']
        },
      },
      {path: 'orderforuser',
        name: 'orderforuser',
        component: orderforuser,
        meta: {
          roles: ['user']
        },
      },
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: login,
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
  },
  {
    path: '/404',
    name: '404',
    component: error,
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

const user = sessionStorage.getItem('user')

router.beforeEach((to, from, next) => {
  if (to.path === '/login' || to.path === '/register' || to.path === '/404' ) {
    next();
  } else {
    let token = localStorage.getItem('Authorization');
    if (token === 'null' || token === '') {
      next('/login');
    } else {
      let user = sessionStorage.getItem('user')
      if(!user){
        next('/login')
      }
      let role = JSON.parse(user).authority
      if(to.meta.roles.includes(role)){
        next()	//放行
      }else{
        next('/404')	//跳到404页面
      }
    }
  }

});

export default router
