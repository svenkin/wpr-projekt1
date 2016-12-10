import vue from 'vue';
import vueRouter from 'vue-router';
import chapter from './app/components/chapter.vue';

vue.use(vueRouter);

const routes = [
    { path: '/chapter', component: chapter }
];
const router = new vueRouter({
    routes // short for routes: routes
});

const app = new vue({
    router
}).$mount('#app');