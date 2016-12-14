<template>
    <div>
        <h1>{{$route.params.name}}</h1>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Autem, cum cumque deleniti dicta dolorum eaque, esse est iste maiores obcaecati omnis optio, quas quo sapiente sed sequi ullam! Alias, consequuntur.</p>

    </div>
</template>

<script>
    import stateData from './../services/stateData.js';
    export default{
        methods: {
        },
        created () {
            let std = new stateData();
            console.log("Data in chapter");
            console.log(std.chapter);
       }
    }


</script>
