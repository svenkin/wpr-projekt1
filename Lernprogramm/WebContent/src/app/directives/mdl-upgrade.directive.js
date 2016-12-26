import Vue from 'vue';

Vue.directive('mdl-upgrade', {
    inserted
});

function inserted(el) {
    componentHandler.upgradeElement(el);
}
