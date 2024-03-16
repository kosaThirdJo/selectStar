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
        :items="comments"
        :headers="headers"
        :items-per-page.sync="itemsPerPage"
        :page="page"
        :items-length="totalItems"
        @page-update="onPageUpdate"
        :sort-by="sortBy"
        :search="search"
        items-per-page-text="VIEW"
        page-text="{0}-{1} / 총 {2}건"
        no-data-text="댓글이 존재하지 않습니다."
        must-sort hover
        class="text-center"
    >
      <template #item.content="{ item }">
        <router-link
            :to="`/meet/${item.meetingId}`"
            class="text-center text-decoration-none text-black"
        >
          {{ item.content }}
        </router-link>
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
        <v-card-title>댓글 삭제 {{ currentItemToDelete && currentItemToDelete.deleted === 0 ? '' : '취소' }}</v-card-title>
        <v-card-text>댓글을 정말 삭제 {{
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

const comments = ref([]);

const headers = [
  {title: '댓글 번호', value: 'commentId', align: 'center', sortable: true},
  // {title: '모임글 번호', value: 'meetingId', align: 'center', sortable: true},
  {title: '내용', value: 'content', align: 'center'},
  {title: '작성자', value: 'userName', align: 'center'},
  {title: '작성일', value: 'creationDate', align: 'center', sortable: true},
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
  {key: 'commentId', order: 'desc'}
]
const confirmationModal = ref(false);
let currentItemToDelete = null;

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
    `admin/meetings/comments`,
    "GET", null, localStorage.getItem("jwtToken")
).then(response => {
  comments.value = response.data;
  totalItems.value = response.totalItems;
}).catch(error => {
  console.error(error);
});

// 모임 글 삭제, 삭제 취소
const deleteItem = (comment) => {
  const changedDelete = comment.deleted === 0 ? 1 : 0;
  const requestData = {
    commentId: comment.commentId,
    deleted: comment.deleted
  };
  api2('admin/meetings/comments/delete', "PATCH", requestData)
      .then(() => {
        comment.deleted = changedDelete;
      })
      .catch(error => {
        console.error(error);
      });
  closeConfirmationModal();
};

</script>

<style src="@/assets/css/admin.css" scoped>
</style>
