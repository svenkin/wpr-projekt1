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
    methods: {
        goToChapter,
        goToSection
    }
});

function goToChapter(event) {
    this.$router.push({ path: `/${event.chapter.id}` });
}

function goToSection(event) {
    this.$router.push({ path: `/${event.chapter.id}/${event.section.id}`});
}