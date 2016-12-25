import Vue from 'vue';
import router from './routing';
import StateData from './app/services/state-data';
import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import lection from './app/components/lection.vue';
import exam from './app/components/exam/exam.vue';

import data from './app/services/dataHandling.js';

const vm = new Vue({
    router,
    el: '#app',
    data: {
        state: StateData.instance
    },
    methods: {
        goToChapter,
        goToLection(lection) {
            this.$router.push({path: '/lection/' + lection.name});
        }
    },
    created() {
      console.log('CREATED')
        data.getChapters().then((result)=> {
//
//            let std = new stateData();
//            std.chapter = "das";
//
//
//            setTimeout(() => {
//                this.menupoints = result;
//            }, 200)
        });
    }
});

function goToChapter(chapter) {
    this.state.chapter = chapter;
    this.$router.push({ path: `/${chapter.id}` });
}