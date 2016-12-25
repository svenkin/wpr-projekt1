import Vue from 'vue';
import vueRouter from 'vue-router';

import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';
import exam from './app/components/exam/exam.vue';

Vue.use(vueRouter);

const routes = [
    {
        path: '/',
        component: index
    },
    {
        path: '/:chapterId',
        component: chapter
    },
    {
        path: '/:chapterId/:sectionId',
        component: lection
    },
    {
        path: '/:chapterId/:sectionId/exam',
        component: exam
    }
];

export default new vueRouter({
    routes
});