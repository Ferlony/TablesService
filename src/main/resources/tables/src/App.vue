<template>
  <MyHeader header_title="WellCUM to Tables Service"/>
  <AuthTable/>
</template>




<script>
import MyHeader from './components/MyHeader.vue';
import AuthTable from './components/AuthTable.vue';

export default {
  name: 'App',
  components: {
    MyHeader,
    AuthTable
  }
}
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}

</style>
