<template>
<section>
    <div v-if="section" class="mdl-grid">
        <div v-if="currentLesson" class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
            <div class="mdl-card__title">
                <h3 class="mdl-card__title-text">{{ section.title }}</h3>
                <h4 class="mdl-card__subtitle-text">{{ currentLesson.title}}</h4>
            </div>
            <div class="mdl-card__supporting-text">
                <p>{{ currentLesson.textContent }}</p>
            </div>
            <div class="mdl-card__actions mdl-card--border">
                <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                   @click.prevent="nextLesson()"
                   v-mdl-upgrade >
                    Weiter
                </a>
            </div>
        </div>
    </div>
</section>
</template>

<script>
export default {
    data,
    computed: {
        currentLesson
    },
    methods: {
        nextLesson
    },
    created
}

function data() {
    return {
        section: null,
        lessons: [],
        currentLessonIndex: 0
    };
}

function currentLesson() {
    return this.lessons[this.currentLessonIndex];
}

function nextLesson() {
    this.currentLessonIndex++;
}

function created() {
    fetch(`sections?chapter-id=${this.$route.params.chapterId}&section-id=${this.$route.params.sectionId}`, { credentials: 'same-origin' })
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.section = body.data)
        .then(() => fetch(`lessons?section-id=${this.section.id}`, { credentials: 'same-origin' }))
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.lessons = body.data);
}
</script>