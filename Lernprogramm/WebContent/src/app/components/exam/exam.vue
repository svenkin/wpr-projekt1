<template>


    <section>
        <div class="mdl-grid" v-if="exam != null && answeredExamResults == null">
            <div class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
                <div class="mdl-card__supporting-text">
                    <exam-progress :max="exam.questions.length" :current="questionNumber"></exam-progress>
                </div>
                <div class="mdl-card__actions mdl-card--border">
                    <exam-question :questions="exam.questions" v-on:updateIndex="updateIndex" v-on:questionResult="updateExamResult" v-on:finishExam="finishExam">

                    </exam-question>
                </div>
            </div>
        </div>
        <div class="mdl-grid" v-if="answeredExamResults != null">
            <div class="mdl-card mdl-cell mdl-cell--12-col mdl-shadow--2dp">
                <div class="mdl-card__supporting-text">
                    <ul class="demo-list-icon mdl-list">
                        <li class="mdl-list__item" v-for="answer in answeredExamResults">
                            <span class="mdl-list__item-primary-content">
                            <i class="material-icons mdl-list__item-icon" v-if="answer.correct" style="color:#4CAF50;">check</i>
                            <i class="material-icons mdl-list__item-icon" v-if="!answer.correct" style="color:#F44336;">close</i>
                               {{answer.text}}
                            </span>
                        </li>
                    </ul>
                </div>
                <div class="mdl-card__actions mdl-card--border">
                    <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" v-on:click="toOverview()">
                        Zurück zur Übersicht
                    </button>
                </div>
            </div>
        </div>
    </section>

</template>

<script>
import progress from './exam-progress.vue';
import question from './exam-question.vue';

export default{
  data,
  created,
  methods: {
    updateIndex,
    updateExamResult,
    finishExam,
    toOverview
  },
  components: {
    'exam-progress': progress,
    'exam-question': question
  },
  events: {
    'questionResult': function(res) {
      console.log('retarded', res);
    }
  }
}

function data(){
    return {
       questionNumber : 1,
       exam: null,
       answeredExamResults : null,
       examAnswers: null
    }
}
function created() {
     fetch(`exam?section-id=${this.$route.params.sectionId}`, { credentials: 'same-origin' })
        .then((resp) => {
            if (resp.ok) return resp.json();
        }).then((resp) => {
            console.log(resp);
            this.exam = resp.data;
        });
        this.examAnswers = {
            sectionId: this.$route.params.sectionId,
            answers: []
        }
    }
function updateIndex(){
    this.questionNumber++;
}

function updateExamResult(result){
    this.examAnswers.answers.push(result);
}

function finishExam(){
    console.log(document.cookie);
    fetch(`exam?section-id=${this.$route.params.sectionId}&chapter-id=${this.$route.params.chapterId}`,
    {
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',

        },
        credentials: 'include',
        method: 'POST',
        body: JSON.stringify(this.examAnswers)
    }).then((resp) => {
        return resp.json();
    }).then((resp) => {
        this.answeredExamResults = resp.data;
    })
}

function toOverview(){
    this.$router.push({ path: `/${this.$route.params.chapterId}`});
}


</script>
