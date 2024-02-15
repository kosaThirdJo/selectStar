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
    </v-data-table>
  </section>
</template>

<script setup>
import {computed, ref} from 'vue';
import {api2, apiToken2} from "@/common.js";
import defaultImg from "@/assets/image/global/userdefaultimg.png";

const users = ref([]);

const headers = [ // data-table header 설정
  {title: '모임글 번호', value: 'meetingId', align: 'center'},
  {title: '제목', value: 'title', align: 'center'},
  {title: '조회수', value: 'views', align: 'center'},
  {title: '작성자', value: 'userName', align: 'center'},
  {title: '작성일', value: 'creationDate', align: 'center'},
  {title: '상태', value: 'status', align: 'center'},
  {title: '삭제', value: 'deleted', align: 'center'},
  {title: '관리', value: 'management', align: 'center'},
];

// 페이징
const itemsPerPage = ref(10);
const page = ref(1);
let totalItems = ref(0);
const onPageUpdate = (newPage) => {
  page.value = newPage;
};

// 글 전체 조회
apiToken2(
    `admin/meetings`,
    "GET", null, localStorage.getItem("jwtToken")
).then(response => {
  users.value = response.data;
  totalItems.value = response.totalItems;
}).catch(error => {
  console.error(error);
});


// 회원 상태 변경
const updateUserStatus = (user) => {
  const requestData = {
    userId: user.userId,
    userStatus: user.selectedUserStatus
  };
  api2(`admin/users`, "PATCH", requestData)
      .then(response=>{
        user.userStatus = user.selectedUserStatus;
      })
      .catch(error=> {
        console.error(error);
      })
};
</script>

<style src="@/assets/css/admin.css" scoped>
</style>
