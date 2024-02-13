<script setup>

</script>
<template>
  <aside class="side-bar-box">
    <div class="admin-title">관리자 페이지</div>
    <div class="admin-menu">
      <router-link class="admin-menu-a" to="/admin/users">회원 관리</router-link>
    </div>
    <div class="admin-menu">모임 게시판</div>
    <div class="admin-menu admin-sub-menu">게시글 관리</div>
    <div class="admin-menu admin-sub-menu">댓글 관리</div>
    <div class="admin-menu">고민 게시판</div>
    <div class="admin-menu admin-sub-menu">게시글 관리</div>
    <div class="admin-menu admin-sub-menu">댓글 관리</div>
  </aside>
</template>

<style src="@/assets/css/admin.css" scoped>
</style>