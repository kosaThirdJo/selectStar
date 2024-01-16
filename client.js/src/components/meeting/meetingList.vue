<style src="../../assets/css/meeting/meeting_home.css" scoped>
.read-the-docs {
  color: #888;
}
</style>
<style src="../../assets/css/home.css" scoped>

</style>
<style scoped>
.frame{
  margin: 40px 10px;
  display: flex;
  flex-direction: column;
}
.button-create-meeting{
  height: 65px;
  /* margin-left: 10px; */
  display: flex;
  justify-content: flex-end;
  margin-right: 15%;
}
.container {
  display:flex;
  flex-flow: row wrap;
  gap: 5px;
  margin-bottom: 20px;
}

.content_list_btn {
  position: relative;
  display: inline-block;
  cursor: pointer;
  outline: none;
  border: 0;
  vertical-align: middle;
  text-decoration: none;
  background: transparent;
  padding: 0;
  font-size: inherit;
  font-family: inherit;
}
.make-meeting {
  width: 210px;
  display: flex;
  align-items: center;
}

.content_list_btn:hover .circle .icon.arrow {
  background: #fff;
  transform: translate(1rem, 0);
}
.arrow{
  transition: all 0.45s cubic-bezier(0.65, 0, 0.076, 1);
  left: 0.625rem;
  width: 1.125rem;
  height: 0.125rem;
  background: none;
}


</style>
<template>
  <div class="frame">
  <div class="button-create-meeting">
    <router-link :to="'/meet/write'" class="content_list_btn make-meeting">
      <span class="circle" aria-hidden="true">
          <span class="icon arrow"></span>
      </span>
      <span class="button-text">모임 만들기</span>
    </router-link>
  </div>
  <div class="container">

    <br>
    <card v-for="(resOne, i) in result" :key="i" :resOne="resOne"></card>
  </div>

  <div class="container mt-5">
    <nav aria-label="Page navigation example" style="margin: auto; display: flex; justify-content: right ">
      <div>
      <ul class="pagination">
        <li  class="page-item">
          <span v-if="offset" class="page-link" @click="changeOffset(-5)" >이전</span>
        </li>
        <li class="page-item"  v-for="(content,idx) in Math.max(0,Math.min(offset+5,totalPage-offset))">
          <router-link class="main-header-nav-meetinglist-text" :to="routerUrl + 'page=' + (idx+parseInt(offset)) + '&offset=' + offset">
          <span class="page-link" v-text= "offset+content" @click="changePage(idx+offset)"></span>
          </router-link>
        </li>
        <li v-if="offset+5< totalPage"  class="page-item"><span class="page-link" @click="changeOffset(+5)">다음</span></li>
      </ul>
      </div>
      <div class="page-item-btn" style=""><router-link id="write-content" :to="'/meet/write'" class="btn btn-primary" style="background-color: #FF9F29; color: white; float: right"> 새 글 작성</router-link></div>

    </nav>
  </div>

  <div>

  </div>
  </div>
</template>

<script setup>

import { ref, watch } from 'vue'
import Card from "../element/card.vue";
import {api} from "@/common.js";
import {useRoute} from "vue-router";
import router from "../../router/index.js";

const route = useRoute();

  const req = ref({
  page:(useRoute().query.page!== undefined) ? parseInt(useRoute().query.page): 0,
  size:(useRoute().query.size!== undefined) ? parseInt(useRoute().query.size): 9,
  order:(useRoute().query.order!== undefined) ? useRoute().query.order: "desc",
  category: (useRoute().query.category!== undefined) ? useRoute().query.category: null,
  criteria:(useRoute().query.criteria!== undefined) ? useRoute().query.criteria: "meetingId",
    offset:(useRoute().query.offset!== undefined) ? parseInt(useRoute().query.offset): 0
})
let offset = (useRoute().query.offset!== undefined) ? parseInt(useRoute().query.offset): 0
getPage()
watch(() => route.query.category, (newCategory,lastCategory) => {
    req.value.category = newCategory;
    getPage()

})
let routerUrl = ref("/meet?")
let routeQuery = route.query

for (const ele in routeQuery){
  if (ele !== "page" && ele !== "offset") {
    routerUrl.value += ele + "=" + routeQuery[ele] +"&"
  }
}
watch(
    () => router.currentRoute.value,
    (newVal,lastVal) => {
      if (lastVal.query.category !== newVal.query.category) {
        req.value.page = 0;
        offset = 0
      }
      routerUrl = ref("/meet?")
      routeQuery = route.query

      for (const ele in routeQuery){
        if (ele !== "page" && ele !== "offset") {
          routerUrl.value += ele + "=" + routeQuery[ele] +"&"
        }
      }
      getPage()
    }
)
const result = ref({});
const totalPage = ref(0);
function changePage(newPage) {
  req.value.page = newPage;
  getPage()
  window.scrollTo(0, 0);
}
function changeOffset(offsetDelta){
  getPage()
  offset = offset + offsetDelta
}
//
async function getPage() {
   api(
      "meeting?" +
      "page=" + (parseInt(req.value.page)) + "&" +
      "size=" + req.value.size + "&" +
      "order=" + req.value.order + "&" +  // '=' 추가
      "criteria=" + req.value.criteria
      + ((req.value.category) ? "&category=" + req.value.category : ""),
      "GET", null
  ).then(response => {
     result.value = response.content
     totalPage.value = response.totalPages;
   })

}

</script>
