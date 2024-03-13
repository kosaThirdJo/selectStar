<template>
  <section>
    <!-- 검색창 -->
    <v-text-field
        v-model="search"
        label="검색"
        prepend-inner-icon="mdi-magnify"
        single-line
        hide-details
        variant="solo"
        density="compact"
        class="w-25 float-right"
    ></v-text-field>
    <!-- 회원 목록 -->
    <v-data-table
        :items="users"
        :headers="headers"
        :items-per-page.sync="itemsPerPage"
        :page="page"
        :items-length="totalItems"
        @page-update="onPageUpdate"
        :sort-by="sortBy"
        :search="search"
        items-per-page-text="VIEW"
        page-text="{0}-{1} / 총 {2}명"
        no-data-text="회원이 존재하지 않습니다."
        must-sort hover
    >
      <!-- 프로필 이미지 -->
      <template #item.profilePhoto="{ item }">
        <img class="profile-img" :src="getProfilePhoto(item.profilePhoto)" alt="Profile Photo"/>
      </template>
      <!-- 회원 상태 -->
      <template #item.userStatus="{ item }">
        <td class="d-flex justify-center">
          <v-chip :class="getUserStatus(item.userStatus)">
            {{ getUserStatusString(item.userStatus) }}
          </v-chip>
        </td>
      </template>
      <!-- 회원 상태 변경 -->
      <template #item.management="{ item }">
        <td class="d-flex justify-center">
          <v-select
              v-model="item.selectedUserStatus"
              :items="userStatusOptions"
              item-title="title"
              item-value="value"
              variant="underlined"
              @update:modelValue="updateUserStatus(item)"
          ></v-select>
        </td>
      </template>
    </v-data-table>
  </section>
</template>

<script setup>
import {ref} from 'vue';
import {api2, apiToken2} from "@/common.js";
import defaultImg from "@/assets/image/global/userdefaultimg.png";

const users = ref([]);
const headers = [ // data-table header 설정
  {title: '회원번호', value: 'userId', align: 'center', sortable: true},
  {title: '프로필 이미지', value: 'profilePhoto', align: 'center'},
  {title: '아이디', value: 'name', align: 'center'},
  {title: '이메일', value: 'email', align: 'center'},
  {title: '가입 날짜', value: 'joinDate', align: 'center', sortable: true},
  {title: '회원 상태', value: 'userStatus', align: 'center', sortable: true},
  {title: '관리', value: 'management', align: 'center'},
];

// 페이징
const itemsPerPage = ref(10);
const page = ref(1);
const totalItems = ref(0);
const onPageUpdate = (newPage) => {
  page.value = newPage;
};
const search = ref('');
const sortBy = [
  {key: 'userId', order: 'desc'}
]

// 회원 상태
const userStatusOptions = [
  {title: '활동', value: 0},
  {title: '정지', value: 1},
  {title: '탈퇴', value: 2}
];

// 회원 전체 조회
apiToken2(
    `admin/users`,
    "GET", null, localStorage.getItem("jwtToken")
).then(response => {
  users.value = response.data.map(user => {
    return {
      ...user,
      selectedUserStatus: user.userStatus
    };
  });
  totalItems.value = response.totalItems;
}).catch(error => {
  console.error(error);
});

// 회원 프로필 이미지
const getProfilePhoto = (photo) => {
  return photo ? photo : defaultImg;
}

// 회원 상태 출력
const getUserStatus = (status) => {
  return status === 0 ? 'text-primary' : (status === 1 ? 'text-warning' : 'text-error');
};
const getUserStatusString = (status) => {
  return status === 0 ? '활동' : (status === 1 ? '정지' : '탈퇴');
};

// 회원 상태 변경
const updateUserStatus = (user) => {
  const requestData = {
    userId: user.userId,
    userStatus: user.selectedUserStatus
  };
  api2(`admin/users`, "PATCH", requestData)
      .then(() => {
        user.userStatus = user.selectedUserStatus;
      })
      .catch(error => {
        console.error(error);
      })
};
</script>

<style src="@/assets/css/admin.css" scoped>
.v-data-table-footer .v-pagination__list {
  margin-bottom: 0 !important;
}
</style>
