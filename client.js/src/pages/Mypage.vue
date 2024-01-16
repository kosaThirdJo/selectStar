<template>
  <div id="frame-all">
    <Sidebar></Sidebar>
    <router-view></router-view>
  </div>
</template>

<script setup>

import Sidebar from "../components/mypages/Sidebar.vue";

</script>

<!--<style scoped>-->
<style>
  #frame-all{
    display: flex;
    flex-direction: row;
    width: 100%;
    min-height: 1300px;
    gap: 40px;
    margin: 10px;
    padding: 20px;
  }
  aside{
    width: 20%;
  }
  section{
    width: 80%;
  }
</style>