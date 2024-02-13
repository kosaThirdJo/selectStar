<script setup>
import { ref } from 'vue';
import { apiToken2 } from "@/common.js";
import defaultImg from "@/assets/image/global/userdefaultimg.png";

const users = ref([]);
const headers = [
  { title: '회원번호', value: 'userId' },
  { title: '프로필 이미지', value: 'profilePhoto' },
  { title: '아이디', value: 'name' },
  { title: '이메일', value: 'email' },
  { title: '가입 날짜', value: 'joinDate' },
  { title: '회원 상태', value: 'userStatus' },
];

apiToken2(
    `admin/users`,
    "GET", null, localStorage.getItem("jwtToken")
).then(response => {
  users.value = response.data;
}).catch(error => {
  console.error(error);
});

const getProfilePhoto = (photo) => {
  return photo ? photo : defaultImg;
}
</script>

<template>
  <section>
    <v-data-table :items="users" :headers="headers" hide-default-footer>
      <template #item.profilePhoto="{ item }">
        <img class="profile-img" :src="getProfilePhoto(item.profilePhoto)" alt="Profile Photo"/>
      </template>
    </v-data-table>
  </section>
</template>
<style src="@/assets/css/admin.css" scoped>
</style>
