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
                <a :disabled="!hasPreviousLesson"
                   @click.prevent="previousLesson()"
                   v-mdl-upgrade
                   class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                   style="float: left;">
                    Zurück
                </a>
                <a :disabled="!hasNextLesson"
                   @click.prevent="nextLesson()"
                   v-mdl-upgrade
                   class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                   style="float: right;">
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
        currentLesson,
        hasNextLesson,
        hasPreviousLesson
    },
    methods: {
        nextLesson,
        previousLesson
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

function hasPreviousLesson() {
    return this.currentLessonIndex > 0;
}

function hasNextLesson() {
    return this.currentLessonIndex < this.lessons.length - 1;
}

function previousLesson() {
    if (this.hasPreviousLesson) this.currentLessonIndex--;
}

function nextLesson() {
    if (this.hasNextLesson) this.currentLessonIndex++;
}

function created() {
    fetch(`sections?chapter-id=${this.$route.params.chapterId}&section-id=${this.$route.params.sectionId}`, { credentials: 'same-origin' })
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.section = body.data)
        .then(() => fetch(`lessons?chapter-id=${this.$route.params.chapterId}&section-id=${this.section.id}`, { credentials: 'same-origin' }))
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(body => this.lessons = body.data);
}
</script>