<script setup>
import { ref } from 'vue';
import { apiToken2 } from "@/common.js";
import defaultImg from "@/assets/image/global/userdefaultimg.png";

const users = ref([]);
const headers = [
  { title: '회원번호', value: 'userId', align: 'center' },
  { title: '프로필 이미지', value: 'profilePhoto', align: 'center' },
  { title: '아이디', value: 'name', align: 'center' },
  { title: '이메일', value: 'email', align: 'center' },
  { title: '가입 날짜', value: 'joinDate', align: 'center' },
  { title: '회원 상태', value: 'userStatus', align: 'center' },
  { title: '관리', value: 'management', align: 'center' },
];
const itemsPerPage = ref(10);
const page = ref(1);
let totalItems = ref(0); // 전체 항목 수
const selectedUserStatus = ref(null);
const userStatusOptions = [
  { value: 0, text: '활동' },
  { value: 1, text: '정지' },
  { value: 2, text: '탈퇴' }
];

apiToken2(
    `admin/users`,
    "GET", null, localStorage.getItem("jwtToken")
).then(response => {
  users.value = response.data;
  totalItems.value = response.totalItems; // 전체 항목 수 설정
}).catch(error => {
  console.error(error);
});
const onPageUpdate = (newPage) => {
  page.value = newPage;
};
const getProfilePhoto = (photo) => {
  return photo ? photo : defaultImg;
}
const getUserStatus = (status) => {
  return status === 0 ? 'text-primary' : (status === 1 ? 'text-warning' : 'text-error');
};
const getUserStatusString = (status) => {
  return status === 0 ? '활동' : (status === 1 ? '정지' : '탈퇴');
};
const updateUserStatus = (userId) => {
};
</script>

<template>
  <section>
    <v-data-table
        :items="users"
        :headers="headers"
        :items-per-page.sync="itemsPerPage"
        :page="page"
        :items-length="totalItems"
        @page-update="onPageUpdate"
        class="text-center"
    >
      <template #item.profilePhoto="{ item }">
        <img class="profile-img" :src="getProfilePhoto(item.profilePhoto)" alt="Profile Photo"/>
      </template>
      <template #item.userStatus="{ item }">
        <td class="">
          <v-chip :class="getUserStatus(item.userStatus)">
            {{ getUserStatusString(item.userStatus) }}
          </v-chip>
        </td>
      </template>
      <template #item.management="{ item }">
        <td class="d-flex justify-center align-center">
          <v-select
              v-model="selectedUserStatus"
              :items="userStatusOptions"
              label="회원 상태"
              outlined
              dense
              @change="updateUserStatus(item.userId)"
          ></v-select>
        </td>
      </template>
    </v-data-table>
  </section>
</template>

<style src="@/assets/css/admin.css" scoped>
</style>
