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
    <!-- 모임 목록 -->
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
        page-text="{0}-{1} / 총 {2}건"
        no-data-text="모임이 존재하지 않습니다."
        must-sort hover
        class="text-center"
    >
      <!-- 모임글 제목 -->
      <template #item.title="{ item }">
        <router-link
            :to="`/meet/${item.meetingId}`"
            class="text-center text-decoration-none text-black"
        >
          {{ item.title }}
        </router-link>
      </template>
      <!-- 모임글 상태 -->
      <template #item.status="{ item }">
        <td class="d-flex justify-center">
          <v-chip
              variant="outlined"
              :color="getMeetingStatus(item.status)">
            {{ getMeetingStatusString(item.status) }}
          </v-chip>
        </td>
      </template>
      <!-- 삭제 상태 -->
      <template #item.deleted="{ item }">
        <td class="d-flex justify-center">
          <v-icon
              :color="getDeleteStatusColor(item.deleted)"
              @click="openConfirmationModal(item)"
          >
            {{ getDeleteStatusIcon(item.deleted) }}
          </v-icon>
        </td>
      </template>
    </v-data-table>
    <!-- 확인 모달 -->
    <v-dialog v-model="confirmationModal" max-width="500px" min-height="500px">
      <v-card>
        <v-card-title>게시물 삭제 {{ currentItemToDelete && currentItemToDelete.deleted === 0 ? '' : '취소' }}</v-card-title>
        <v-card-text>글을 정말 삭제 {{
            currentItemToDelete && currentItemToDelete.deleted === 0 ? '' : '취소'
          }}하시겠습니까?
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn color="error" @click="deleteItem(currentItemToDelete)">삭제
            {{ currentItemToDelete && currentItemToDelete.deleted === 0 ? '' : '취소' }}
          </v-btn>
          <v-btn @click="closeConfirmationModal">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </section>
</template>

<script setup>
import {ref} from 'vue';
import {api2, apiToken2} from "@/common.js";

const users = ref([]);

const headers = [ // data-table header 설정
  {title: '모임글 번호', value: 'meetingId', align: 'center', sortable: true},
  {title: '제목', value: 'title', align: 'center'},
  {title: '조회수', value: 'views', align: 'center', sortable: true},
  {title: '작성자', value: 'userName', align: 'center'},
  {title: '작성일', value: 'creationDate', align: 'center', sortable: true},
  {title: '상태', value: 'status', align: 'center'},
  {title: '삭제', value: 'deleted', align: 'center'},
];

// 페이징
const itemsPerPage = ref(10);
const page = ref(1);
let totalItems = ref(0);
const onPageUpdate = (newPage) => {
  page.value = newPage;
};
const search = ref('');
const sortBy = [
  {key: 'meetingId', order: 'desc'}
]
const confirmationModal = ref(false);
let currentItemToDelete = null;
// 모임 상태 출력
const getMeetingStatus = (status) => {
  return status === 0 ? 'orange' : 'grey';
};
const getMeetingStatusString = (status) => {
  return status === 0 ? '모집 중' : '모집 완료';
};

// 삭제 상태 출력
const getDeleteStatusIcon = (status) => {
  return status === 0 ? 'mdi-delete' : 'mdi-check';
};

const getDeleteStatusColor = (status) => {
  return status === 0 ? 'black' : 'grey';
};
// 삭제 상태 확인 모달 (열기)
const openConfirmationModal = (item) => {
  currentItemToDelete = item;
  confirmationModal.value = true;
};

// 삭제 상태 확인 모달 (닫기)
const closeConfirmationModal = () => {
  currentItemToDelete = null;
  confirmationModal.value = false;
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


// 모임 글 삭제, 삭제 취소
const deleteItem = (meeting) => {
  const changedDelete = meeting.deleted === 0 ? 1 : 0;
  const requestData = {
    meetingId: meeting.meetingId,
    deleted: meeting.deleted
  };
  api2('admin/meetings/delete', "PATCH", requestData)
      .then(() => {
        meeting.deleted = changedDelete;
      })
      .catch(error => {
        console.error(error);
      });
  closeConfirmationModal(); // 모달 닫기
};

</script>

<style src="@/assets/css/admin.css" scoped>
</style>
