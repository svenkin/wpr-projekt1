<template>
    <section>
        <div class="mdl-grid" v-if="user != null">
            <div class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
                <div class="mdl-card__title">
                    <h3 class="mdl-card__title-text">{{ user.nickName}}</h3>
                </div>
                <div class="mdl-card__supporting-text">
                    <ul class="demo-list-item mdl-list">
                        <li class="mdl-list__item">
                            <span class="mdl-list__item-primary-content">
                              Vorname: {{user.firstName}}
                            </span>
                        </li>
                        <li class="mdl-list__item">
                            <span class="mdl-list__item-primary-content">
                              Nachname: {{user.lastName}}
                            </span>
                        </li>
                        <li class="mdl-list__item">
                            <span class="mdl-list__item-primary-content">
                              Geschlecht: {{user.gender}}
                            </span>
                        </li>
                    </ul>
                </div>
                <div class="mdl-card__supporting-text" v-if="user.answeredQuestions.length > 0">
                    <h4>Bereits Beantwortete Fragen</h4>
                    <p>Es wurden {{user.correctAnswered}} von {{user.answeredQuestions.length}} Fragen richtig beantwortet.</p>
                    <ul class="demo-list-item mdl-list answers">
                        <li class="mdl-list__item" v-for="answer in user.answeredQuestions">
                            <span class="mdl-list__item-primary-content">
                                <i class="material-icons mdl-list__item-icon" v-if="answer.correct" style="color:#4CAF50;">check</i>
                                <i class="material-icons mdl-list__item-icon" v-if="!answer.correct" style="color:#F44336;">close</i>
                                {{answer.question}}
                            </span>
                        </li>
                    </ul>
                </div>
                <div class="mdl-card__actions mdl-card--border">
                    <router-link class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" to="/" mdl-upgrade>Zur Kapitelübersicht</router-link>
                </div>
            </div>
        </div>
    </section>
</template>
<style scoped>
    ul{
        margin: 0;
    }
    .answers {
        max-height: 400px;
        overflow-y: scroll;
    }

</style>
<script>
    export default{
        data,
        created
    }
    function data(){
         return{
            user: null
         }
    }
    function created(){
      fetch('user', { credentials: 'same-origin' })
        .then((response) => {
            if (response.ok) return response.json();
        }).then((body) => {
            this.user = body.data
        })
    }

</script>
