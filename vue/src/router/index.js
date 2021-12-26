import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import login from "../views/login";
import layout from "../layout/Layout";
import Register from "../views/Register";
import userspace from "../views/userspace";
import orderlist from "@/views/orderlist";
import Car from "@/views/Car";
import Driver from "@/views/Driver";
import orderfordriver from "@/views/orderfordriver";
import carfordriver from "@/views/Carfordriver";
import orderforuser from "@/views/orderforuser";

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
      },
      {path: 'userspace',
        name: 'userspace',
        component: userspace,
      },
      {path: 'orderlist',
        name: 'orderlist',
        component: orderlist,
      },
      {path: 'car',
        name: 'Car',
        component: Car,
      },
      {path: 'driver',
        name: 'Driver',
        component: Driver,
      },
      {path: 'orderfordriver',
        name: 'orderfordriver',
        component: orderfordriver,
      },
      {path: 'carfordriver',
        name: 'carfordriver',
        component: carfordriver,
      },
      {path: 'orderforuser',
        name: 'orderforuser',
        component: orderforuser,
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
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next();
  } else {
    let token = localStorage.getItem('Authorization');
    if (token === 'null' || token === '') {
      next('/login');
    } else {
      next();
    }
  }

});

export default router
