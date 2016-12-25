import Vue from 'vue';
import vueRouter from 'vue-router';

import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';

import data from './app/services/dataHandling.js';
import stateData from './app/services/stateData.js';


Vue.use(vueRouter);

const routes = [
    { path: '/', component: index },
    { path: '/:chapterId', component: chapter },
    { path: '/:chapterId/:sectionId', component: lection }
];
const router = new vueRouter({
    routes
});

const app = new Vue({
    router,
    el: '#app',
    data: {
        menupoints: {},
        chapterRoute: "/chapter/",
        lectionRoute: "/lection/"
    },
    methods: {
        goToChapter(chapter) {
            this.$router.push({ path: '/chapter/' + chapter.name });
        },
        goLection(lection) {
            this.$router.push({path: '/lection/' + lection.name});
        }
    },
    created() {
        data.getChapters().then((result)=> {

            let std = new stateData();
            std.chapter = "das";


            setTimeout(() => {
                this.menupoints = result;
            }, 200)
        });
    }
});
