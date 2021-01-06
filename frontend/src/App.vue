<template>
  <div id="app">    
    <router-view v-if="isRouterAlive"/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data(){
    return{
       isRouterAlive: true
    }
  },
  provide(){
    return{
      reload:this.reload
    }
  },
  methods:{
    reload() {
     this.isRouterAlive = false
     this.$nextTick(() => (this.isRouterAlive = true))
   }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
body{
  margin:0;
}
a{
    text-decoration: none;
}
.container{
  margin-left:250px;
  padding:30px 5px;
}
</style>
