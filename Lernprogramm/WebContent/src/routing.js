import Vue from 'vue';
import vueRouter from 'vue-router';

import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import section from './app/components/section.vue';
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
        component: section
    },
    {
        path: '/:chapterId/:sectionId/exam',
        component: exam
    }
];

export default new vueRouter({
    routes
});