<script setup>

import AdminSidebar from "@/components/admin/adminSidebar.vue";
</script>

<template>
  <div class="admin-component">
    <admin-sidebar></admin-sidebar>
    <router-view></router-view>
  </div>
</template>

<style src="@/assets/css/admin.css" scoped>
</style>
