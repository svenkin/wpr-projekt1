import Vue from 'vue';

import router from './routing';
import StateData from './app/services/state-data';

const vm = new Vue({
    router,
    el: '#app',
    data: {
        state: StateData.instance
    },
    methods: {
        goToChapter
    }
});

function goToChapter(chapter) {
    this.state.chapter = chapter;
    this.$router.push({ path: `/${chapter.id}` });
}