import vue from 'vue';
import vueRouter from 'vue-router';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';
import data from './app/services/dataHandling.js';
import stateData from './app/services/stateData.js';


vue.use(vueRouter);

const routes = [
    {path: '/chapter/:name', component: chapter},
    {path: '/lection/:name', component: lection}
];
const router = new vueRouter({
    routes // short for routes: routes
});

const app = new vue({
    router,
    data: {
        menupoints: {},
        chapterRoute: "/chapter/",
        lectionRoute: "/lection/"
    },
    methods: {
        goChapter: function (chapter) {
            this.$router.push({path: '/chapter/' + chapter.name});
        },
        goLection: function (lection) {
            this.$router.push({path: '/lection/' + lection.name});
        }
    },
    created: function () {
        data.getChapters().then((result)=> {

            let std = new stateData();
            std.chapter = "das";


            setTimeout(() => {
                this.menupoints = result;
            }, 200)
        });
    }
}).$mount('#app');
