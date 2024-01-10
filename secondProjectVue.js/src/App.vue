<style scoped src="./assets/css/home.css">
</style>

<template>
  <PageHeader></PageHeader>
  <router-view/>
  <PageFooter></PageFooter>
</template>

<script setup>
import PageHeader from "./components/common/TheHeader.vue";
import PageFooter from "./components/common/TheFooter.vue";
</script>

