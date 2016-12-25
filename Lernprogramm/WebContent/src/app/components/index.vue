<template>
<section>
    <div v-if="chapters.length" class="mdl-grid">
        <div v-for="chapter in chapters" class="mdl-card mdl-cell mdl-cell--6-col mdl-shadow--2dp">
            <div class="mdl-card__title">
                <h3 class="mdl-card__title-text">{{ chapter.title }}</h3>
            </div>
            <div class="mdl-card__supporting-text">
                <p>{{ chapter.description }}</p>
            </div>
            <div class="mdl-card__actions mdl-card--border">
                <ul class="mdl-list">
                    <li class="mdl-list__item"><a><!-- TODO: dynamisch --></a></li>
                </ul>
            </div>
        </div>
    </div>
</section>
</template>

<script>
export default {
    data() {
        return {
            chapters: []
        };
    },
    created
}

function created() {
    fetch('chapters', { credentials: 'same-origin' }).then(response => {
        if (response.ok) response.json().then(json => this.chapters = json.data);
    });
}
</script>

<style>
.mdl-card__title {
    height: 300px;
    color: #fff;
    background-image: url('~../../../res/whiskey-title.jpg');
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover
}
</style>