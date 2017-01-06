<template>
    <div class="exam-progress">
        <div class="bar">
            <div class="bg"></div>
            <div class="progress" v-bind:style="{width : width}"></div>
        </div>
        Frage {{current}} von {{max}}
    </div>
</template>

<style scoped>
.exam-progress{
  position: relative;
  font-size: 20px;
}
.exam-progress .bar{
  float: left;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  margin-left: 200px;
  width: 50%;
  height: 10px;
}
.bar .bg {
  background-color: #3f51b5;
  opacity: 0.5;
  width: 100%;
  height: 100%;
  position: absolute;
}
.bar .progress{
  height: 100%;
  background-color: #3f51b5;
  position: absolute;
}

</style>
<script>

export default{
  data: () => {
    return {
      width: '0%'
    }
  },
  props : ['max','current'],
  watch : {
        current : function (value) {
          if(value >= 0 && this.max >= value){
            this.width = ((100/this.max)*value)+'%';
          }
        }
      },
  created
}

function created(){
    //Set width on init
    if(this.current >= 0 && this.max >= this.current){
        this.width = ((100/this.max)*this.current)+'%';
      }
}
</script>
