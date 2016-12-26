import Vue from 'vue';
import router from './routing';

import './app/directives/mdl-upgrade.directive';
import StateData from './app/services/state-data';

import index from './app/components/index.vue';
import chapter from './app/components/chapter.vue';
import section from './app/components/section.vue';
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
        goToSection
    }
});

function goToChapter(chapter) {
    this.state.chapter = chapter;
    this.$router.push({ path: `/${this.state.chapter.id}` });
}

function goToSection(section) {
    this.state.section = section;
    this.$router.push({ path: `/${this.state.chapter.id}/${this.state.section.id}`});
}