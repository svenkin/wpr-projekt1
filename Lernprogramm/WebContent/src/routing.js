import Vue from 'vue';
import vueRouter from 'vue-router';

import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';

Vue.use(vueRouter);

const routes = [
    { path: '/', component: index },
    { path: '/:chapterId', component: chapter },
    { path: '/:chapterId/:sectionId', component: lection }
];

export default new vueRouter({
    routes
});
