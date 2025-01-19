import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '@/views/HomeView.vue'
import PersonView from '@/views/persons/PersonView.vue'
import PersonProfileView from '@/views/personProfile/PersonProfileView.vue'
import ResourceView from '@/views/resources/ResourceView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: HomeView
    },
    {
      path: '/contact',
      name: 'contact',
      component: HomeView
    },
    {
      path: '/persons',
      name: 'persons',
      component: PersonView
    },
    {
      path: '/persons/:personId',
      name: 'personProfile',
      component: PersonProfileView,
	  props: true
    },
    {
      path: '/resources',
      name: 'resources',
      component: ResourceView
    },
    {
      path: '/register',
      name: 'register',
      component: HomeView
    },
    {
      path: '/profile',
      name: 'profile',
      component: HomeView
    },
    {
      path: '/admin',
      name: 'admin',
      component: HomeView
    }
  ]
})

export default router
