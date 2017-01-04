<template>
<section>
    <div v-if="chapters.length" class="mdl-grid">
        <div v-for="chapter in chapters" class="mdl-card mdl-cell mdl-cell--6-col mdl-shadow--2dp">
            <div v-bind:style="{ backgroundImage: `url(res/${chapter.banner})` }" class="mdl-card__title">
                <h3 class="mdl-card__title-text">{{ chapter.title }}</h3>
            </div>
            <div class="mdl-card__supporting-text">
                <p>{{ chapter.description }}</p>
            </div>
            <div class="mdl-card__actions mdl-card--border">
                <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                   @click.prevent="goToChapter(chapter)"
                   v-mdl-upgrade >
                    Zum Kapitel {{ chapter.title }}
                </a>
            </div>
        </div>
    </div>
</section>
</template>

<script>
export default {
    data,
    methods: {
        goToChapter
    },
    created
}

function data() {
    return {
        chapters: []
    };
}

function goToChapter(chapter) {
    this.$emit('gotochapter', { chapter });
}

function created() {
    fetch('chapters', { credentials: 'same-origin' })
        .then(response => {
            if (response.ok) response.json().then(body => this.chapters = body.data);
        });
    
}
</script>

<style scoped>
.mdl-card__title {
    height: 300px;
    color: #fff;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover
}
</style>