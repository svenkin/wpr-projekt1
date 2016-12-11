import vue from 'vue';
import vueRouter from 'vue-router';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';
import data from './app/services/dataHandling.vue';



vue.use(vueRouter);

const routes = [
    { path: '/chapter/:name', component: chapter },
    { path: '/lection/:name', component: lection }
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
    created: function () {
        data.getChapters().then((data)=>{
            setTimeout(() => {
                this.menupoints = {
                    data:[
                        {
                            name: "Kapitel",
                            lections: [
                                {
                                    name: "Lektion 1"
                                },
                                {
                                    name: "Lektion 2"
                                }
                            ]
                        },
                        {
                            name: "Kapitel 2",
                            lections: [
                                {
                                    name: "Lektion 1"
                                },
                                {
                                    name: "Lektion 2"
                                }
                            ]
                        }
                    ]
                };
            },2000)
        });
    }
}).$mount('#app');