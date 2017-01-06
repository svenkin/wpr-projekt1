<template>
    <div v-if="questions.length > 0">
        <h3>{{currentQuestion.text}}</h3>
        <div v-if="currentQuestion.type === 'SINGLE'">
            <label v-for="(choice,index) in currentQuestion.choices" class="mdl-radio mdl-js-radio mdl-js-ripple-effect" :for="index" v-mdl-upgrade>
                <input type="radio" :id="index" class="mdl-radio__button" :value="choice.id" name="single" v-model="singleResult">
                <span class="mdl-radio__label">{{choice.text}}</span>
            </label>
        </div>

        <div v-if="currentQuestion.type === 'TEXT'">
            <div class="mdl-textfield mdl-js-textfield" v-mdl-upgrade>
                <textarea class="mdl-textfield__input" type="text" rows="5" id="textfield" v-model="textResult"></textarea>
                <label class="mdl-textfield__label" for="textfield">Bitte Lösung eintippen</label>
            </div>
        </div>

        <div v-if="currentQuestion.type === 'MULTIPLE'" class="else">
            <label v-for="(choice,index) in currentQuestion.choices" class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect das" :for="index" v-mdl-upgrade>
                <input type="checkbox" :id="index" class="mdl-checkbox__input" :value="choice.id" name="multiple" v-model="multipleResult">
                <span class="mdl-checkbox__label">{{choice.text}}</span>
            </label>
        </div>

        <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" v-on:click="nextQuestion()">
            {{buttonText}}
        </button>

    </div>
</template>
<style scoped>
 label.mdl-radio {
    display: block;
 }
 .mdl-textfield{
    width: 100%;
 }
 .mdl-checkbox, .mdl-radio{
    margin: 10px 5px;
 }
 .mdl-checkbox:last-child, .mdl-radio:last-child{
    margin-bottom: 24px;
 }



</style>
<script>

export default{
    data(){
        return{
            questionIndex : 0,
            buttonText: 'Nächste Frage',
            currentQuestion: null,
            singleResult : '',
            textResult: '',
            multipleResult: [],
            results : {}
        }
    },
    props : ['questions'],
    methods:{
        nextQuestion
    },
    created
}
function nextQuestion(){

    //Update exam Result
    var result = {};
    switch(this.currentQuestion.type){
    case 'MULTIPLE':
            result = {
                answer: this.multipleResult,
                questionId: this.currentQuestion.questionId,
                type: 'MULTIPLE'
            }
        break;
    case 'SINGLE':
        result = {
            answer: this.singleResult,
            questionId: this.currentQuestion.questionId,
            type: 'SINGLE'
        }
        break;
    case 'TEXT':
        result = {
            answer: this.textResult,
            questionId: this.currentQuestion.questionId,
            type: 'TEXT'
        }
        break;
    }
    this.$emit('questionResult',result)

    //If current question is last question finish exam
    if(this.questionIndex + 1 === this.questions.length){
        this.$emit('finishExam');
    }

    //If next Question is not last question update index
    if(this.questionIndex + 1 < this.questions.length){
        this.questionIndex ++;
        this.currentQuestion = this.questions[this.questionIndex];

        this.$emit('updateIndex');
        //If next question is last question, update button
        if(this.questionIndex + 1 === this.questions.length){
            this.buttonText = 'Test beenden';
        }
    }
    componentHandler.upgradeElement(this.$el);
}
function created(){
    this.currentQuestion = this.questions[this.questionIndex];
}


</script>
