<script setup>

import AdminSidebar from "@/components/admin/adminSidebar.vue";
import { useAuthStore } from '@/stores/index';
import {useRouter} from "vue-router";
import {onMounted} from "vue";
const auth = useAuthStore();
const role = auth.getRole();
const router = useRouter();

const checkRole = () => {
  if (role !== 'ADMIN') {
    alert('권한이 없습니다. 메인 페이지로 이동합니다.');
    router.push('/');
  }
};

onMounted(() => {
  checkRole();
});
</script>

<template>
  <div class="admin-component">
    <admin-sidebar></admin-sidebar>
    <router-view></router-view>
  </div>
</template>

<style src="@/assets/css/admin.css" scoped>
</style>
