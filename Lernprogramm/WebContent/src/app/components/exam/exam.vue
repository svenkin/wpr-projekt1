<template>
<div>
  <input v-model="cur" type="number">
 <br>
  <exam-progress max="5" :current="cur"></exam-progress>
</div>


</template>

<script>
import progress from './exam-progress.vue';

export default{
  data: function () {
     return {
       cur : 0
     }
   },
  created(){

  },
  components: {
    'exam-progress': progress
  }
}
</script>
