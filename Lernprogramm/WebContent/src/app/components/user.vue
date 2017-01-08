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
                <div class="mdl-card__actions mdl-card--border">
                    <router-link class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" to="/" mdl-upgrade>Zur Kapitelübersicht</router-link>
                </div>
            </div>
        </div>
    </section>
</template>
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
