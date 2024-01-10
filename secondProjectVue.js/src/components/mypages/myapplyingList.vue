<template>
  <section class="frame-content">
    <!-- 1. TITLE(내가 만든 모임) -->
    <div class="frame-applymeeting-title">
      <div class="applymeeting-title">
        <span class="applymeeting-title-text">내가 참여한 모임</span>
      </div>
    </div>
    <!-- 2. CONTENT -->
    <div class="frame-applymeeting-body">
      <!-- 카테고리(전체, 프로젝트, 스터디, 기타)    메뉴 -->
      <div class="search-filter">
        <ul class="search-categry">
          <li class="e-category">
            <button class="filter-button"
                    data-category="category"
                    @click="chkCateSts()"
                    value="all"
                    :class="{activeCate: ischkC['all']}">전체</button>
          </li>
          <li class="e-category">
            <button class="filter-button"
                    data-category="category"
                    @click="chkCateSts()"
                    value="project"
                    :class="{activeCate: ischkC['project']}">프로젝트</button>

          </li>
          <li class="e-category">
            <button class="filter-button"
                    data-category="category"
                    @click="chkCateSts()"
                    value="study"
                    :class="{activeCate: ischkC['study']}">스터디</button>
          </li>
          <li class="e-category">
            <button class="filter-button"
                    data-category="category"
                    @click="chkCateSts()"
                    value="etc"
                    :class="{activeCate: ischkC['etc']}">기타</button>
          </li>
        </ul>
      </div><!-- 2.1.카테고리 메뉴 끝 -->
      <!-- 2.2. 목록 -->
      <div class="mymeeting-list-container">
        <!-- 모집상태(전체, 모집중, 모집완료) 메뉴-->
        <div class="list-container-header">
          <ul class="search-filter-select">
            <li class="e-status">
              <button class="filter-button"
                      data-category="status"
                      @click="chkCateSts()"
                      value="all"
                      :class="{activeCate: ischkS['all']}">전체</button>
            </li>
            <li class="e-status">
              <button class="filter-button"
                      data-category="status"
                      @click="chkCateSts()"
                      value="statusing"
                      :class="{activeCate: ischkS['statusing']}">모집중</button>

            </li>
            <li class="e-status">
              <button class="filter-button"
                      data-category="status"
                      @click="chkCateSts()"
                      value="statused"
                      :class="{activeCate: ischkS['statused']}">모집완료</button>

            </li>
          </ul>
        </div>
        <!-- 2.2.2. 목록 -->
        <div class="list-container-body">
          <!-- 2.2.2.1. 조회 결과(meetingvoList)가 있을 경우 -->
          <div v-if="resultList.length>0">
          <mymeeting v-for="(data, idx) in resultList.slice(0, showCount)" :key="idx" :meetingone="data"></mymeeting>
          </div>
          <!-- 더보기 버튼 -->
          <div class="frame-search-morebtn" v-if="resultList.length>showCount">
            <button class="search-morebtn" @click="showMore()">
              <div class="search-morebtn-icon"><span class="material-icons">expand_more</span></div>
              <span class="search-morebtn-text"><span>더보기</span></span>
            </button>
          </div>
          <!-- 2.2.2.2. 조회 결과(meetingvoList)가 없을 경우 -->
          <div class="frame-errormsg" v-if="!resultList.length"> {{errorMsg}}</div>
        </div><!-- 2.2.2. 목록 끝 -->
      </div><!-- 2.2. 목록 끝 -->
    </div><!--2.CONTENT 끝 -->
  </section>
</template>

<script setup>
import Mymeeting from "./mymeeting.vue";
import axios, {AxiosError} from "axios";
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {api, apiToken} from "../../common.js";

const route = useRoute();
const resultList = ref([]);
//조건별 조회
const selectedFilters = ref({category: "all", status: "all"});
//조건별 조회(클래스 스타일 바인딩 )
const ischkC = ref({ all: false, project: false, study: false, etc: false });
const ischkS = ref({all: false, statusing: false, statused: false});
const errorMsg = ref("");
//토큰
const token = localStorage.getItem('jwtToken');
const showCount = ref(5); //더보기(기본 5개)

function chkCateSts(){
  let targetBtn = event.target;
  let dataCategory = targetBtn.getAttribute("data-category");
  let dataValue = targetBtn.getAttribute("value");

  if(dataCategory==="category"){
    selectedFilters.value.category = dataValue;
    for(let c in ischkC.value){
      ischkC.value[c] = false;
    }
    ischkC.value[dataValue] = true;
  }

  if(dataCategory !== selectedFilters.value.category){
    selectedFilters.value.status = "all";
    for(let s in ischkS.value){
      ischkS.value[s] = false;
    }
    ischkS.value['all'] = true;
  }
  if(dataCategory === "status"){
    selectedFilters.value.status = dataValue;
    for(let s in ischkS.value){
      ischkS.value[s] = false;
    }
    ischkS.value[dataValue] = true;
  }

  apiToken(
      "users/myapplyingfilter?category="+selectedFilters.value.category+
      "&status="+selectedFilters.value.status, "GET", null, token)
      .then(response => {
        if(response instanceof Error){
          let errorRes = response;
          //에러처리코드
          errorMsg.value = errorRes.response.data;
          resultList.value = [];
        }else {
          showCount.value = 5;
          resultList.value = response;
        }
      })
}

//데이터 조회
async function getData(){
  try{
    const res = await axios.get("http://43.201.149.206:8081/users/myapplying", {
      headers:{
        Authorization: token
      }
    });
    showCount.value = 5;
    resultList.value = res.data;
    selectedFilters.value.category = 'all';
    selectedFilters.value.status = 'all';
  }catch (error){
    console.log(error.response);
    errorMsg.value = error.response.data;
  }
}
onMounted(()=>{
  console.log("myapplyingList onMount");
  getData();
})
const showMore = () => {
  showCount.value += 5;
};
</script>

<style src="../../assets/css/applymeetinglistView.css" scoped>
.activeCate{
  color: #FF9F29;
}
</style>