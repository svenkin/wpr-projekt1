<template>
<section>
    <div v-if="chapter" class="mdl-grid">
        <div class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
            <div v-bind:style="{ backgroundImage: `url(res/${chapter.banner})` }" class="mdl-card__title">
                <h3 class="mdl-card__title-text">{{ chapter.title }}</h3>
            </div>
            <div class="mdl-card__supporting-text">
                <p>{{ chapter.description }}</p>
            </div>
            <div class="mdl-card__actions mdl-card--border">
                <ul class="mdl-list">
                    <li v-for="section in sections" @click.prevent="goToSection(chapter, section)" class="mdl-list__item mdl-list__item--three-line">
                        <span class="mdl-list__item-primary-content">
                        <span>{{ section.title }}</span>
                        <span class="mdl-list__item-text-body">{{ section.description }}</span>
                        </span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</section>
</template>

<script>
export default {
    data,
    methods: {
        goToSection
    },
    created
}

function data() {
    return {
        chapter: null,
        sections: []
    };
}

function goToSection(chapter, section) {
    this.$emit('gotosection', { chapter, section });
}

function created() {
    fetch(`chapters?chapter-id=${this.$route.params.chapterId}`, { credentials: 'same-origin' })
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.chapter = body.data)
        .then(() => fetch(`sections?chapter-id=${this.chapter.id}`, { credentials: 'same-origin' }))
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.sections = body.data);
}
</script>

<style scoped>
.mdl-card__title {
    height: 500px;
    color: #fff;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover
}

.mdl-list__item:hover {
    background-color: rgba(158,158,158,.2);
    cursor: pointer;
}
</style>
