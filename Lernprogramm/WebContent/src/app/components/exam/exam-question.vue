<template>
    <div>
        <h1>{{question.question}}</h1>
    </div>
</template>
<style>
    body{
        background-color:#ff0000;
    }
</style>
<script>
    import HeaderComponent from './components/header.vue'
    import OtherComponent from './components/other.vue'
    export default{
        data(){
            return{
                msg:'hello vue'
            }
        },
        props : ['question','result'],
        components:{
            'other-component':OtherComponent,
            HeaderComponent,
        }
    }
</script>
