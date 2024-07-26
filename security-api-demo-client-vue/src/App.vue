<template>
  <div id="app">
    <RsaExample />
  </div>
</template>

<script>
import RsaExample from './views/RsaExample.vue';

export default {
  name: 'App',
  components: {
    RsaExample
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
