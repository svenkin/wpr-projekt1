<template>
<section>
    <div class="mdl-grid">
        <div class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
            <div v-bind:style="{ backgroundImage: `url(res/${chapter.banner})` }" class="mdl-card__title">
                <h3 class="mdl-card__title-text">{{ chapter.title }}</h3>
            </div>
            <div class="mdl-card__supporting-text">
                <p>{{ chapter.description }}</p>
            </div>
            <div class="mdl-card__actions mdl-card--border">
                <ul class="mdl-list">
                    <li v-for="section in sections" class="mdl-list__item mdl-list__item--three-line">
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
    props: ['state'],
    data,
    created
}

function data() {
    return {
        chapter: this.state.chapter,
        sections: []
    };
}

function created() {
    fetch(`sections?chapter-id=${this.$route.params.chapterId}`, { credentials: 'same-origin' })
        .then(response => {
            if (response.ok) response.json().then(body => this.sections = body.data);
        });
}
</script>
