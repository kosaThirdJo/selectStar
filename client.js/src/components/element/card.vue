<style src="../../assets/css/meeting/meeting_home.css" scoped>
.read-the-docs {
  color: #888;
}
</style>
<style src="../../assets/css/home.css" scoped>
</style>
<template>
  <div class="content_list_title_display">
    <div class="container-content">
      <router-link :to="url" class="button">
<!--  <a :href="url" class="button">//https://velog.io/@mahns/vue-router-link -->
      <div class="main-left-content-bottom-meeting" :class="{'main-left-content-bottom-meeting-end': (status==='모집완료')}">
        <!-- 01-02-01 white box -->
        <div class="main-left-content-bottom-meeting-white">
          <!-- white box - top -->
          <div class="main-left-content-bottom-meeting-white-top">
            <!-- white box - top - category -->
            <div class="main-left-content-bottom-meeting-white-top-category">
              <!-- 0: 스터디, 1:프로젝트, 2: 기타 -->
              <span v-text="category" class="main-left-content-bottom-meeting-white-top-text"></span>
            </div>
            <!-- white box - top - status -->
            <div class="main-left-content-bottom-meeting-white-top-status" :class="{'main-left-content-bottom-meeting-white-top-status-end':(status==='모집완료')}">
              <!-- 0: 모집중, 1:모집완료 -->
              <span v-text="status" class="main-left-content-bottom-meeting-white-top-text"></span>
              <!--  스타일 색 다르게-->
            </div>
          </div>

          <!-- white box - bottom -->
          <div class="main-left-content-bottom-meeting-white-bottom">
            <!-- white box - location -->
            <div class="main-left-content-bottom-meeting-white-location">
              <img src="../../assets/image/global/location01.png" alt="">
              <span v-text="location" class="main-left-content-bottom-meeting-white-location-text"
              ></span>
            </div>

            <!-- white box - title -->
            <div class="main-left-content-bottom-meeting-white-title">
              <span v-text="title"></span>
            </div>

            <!--  작성자  -->
            <div class="main-left-content-bottom-meeting-white-deadline">
              <img class="person-img" src="../../assets/image/global/person.png" alt="">
              <span class="main-left-content-bottom-meeting-white-deadline-text">작성자</span>
              <span v-text="writer" class="main-left-content-bottom-meeting-white-deadlineText" style="margin-left: 10px;"></span>
            </div>

            <!-- whitebox - deadline -->
            <div class="main-left-content-bottom-meeting-white-deadline">
              <img src="../../assets/image/global/end_date01.png" alt="">
              <span  class="main-left-content-bottom-meeting-white-deadline-text">마감일</span>
              <span v-text="endDate" class="main-left-content-bottom-meeting-white-deadlineText" style="margin-left: 10px;"></span>
            </div>
          </div>
        </div>

        <!-- 01-02-02 yellow box -->
        <!-- yellowbox - now -->
        <div class="main-left-content-bottom-meeting-yellow-now">
          <!-- yellowbox - now - people -->
          <div class="main-left-content-bottom-meeting-yellow-now-people">
            <img src="../../assets/image/global/member_count.png" alt="">
            <div class="main-left-content-bottom-meeting-yellow-now-people-text">
              현재 <span v-text="nowCount" class="main-left-content-bottom-meeting-yellow-now-people-text-cnt"></span>
              <span v-text=" (status==='모집완료')? '명이 참여하고 있습니다.' : '명이 참여중입니다!'"></span>
            </div>
          </div>
          <!-- yellow - now - info -->
          <div class="main-left-content-bottom-meeting-yellow-now-info">
            <!-- view -->
            <div class="main-left-content-bottom-meeting-yellow-now-info-view">
              <img src="../../assets/image/global/see_count01.png" alt="">
              <div class="main-left-content-bottom-meeting-yellow-now-info-view-cnt">
                <span v-text="viewCount"></span>
              </div>
            </div>
            <!-- reply -->
            <div class="main-left-content-bottom-meeting-yellow-now-info-reply">
              <img src="../../assets/image/home/chat.png" alt="">
              <div class="main-left-content-bottom-meeting-yellow-now-info-reply-cnt">
                <span v-text="commentCount"></span>
              </div>
            </div>
            <!--&lt;!&ndash; heart &ndash;&gt;
            <div class="main-left-content-bottom-meeting-yellow-now-info-heart">
                <div class="main-left-content-bottom-meeting-yellow-now-info-view-icon">
                    <img th:src="@{/image/home/heart.png}">
                </div>
            </div>-->
          </div>
        </div>

        <!-- yellowbox - hashtag -->
        <div class="main-left-content-bottom-meeting-yellow-hashtag">
          <div class="main-left-content-bottom-meeting-yellow-hashtag-text">
            <span v-for="(lang,i1) in langArr" v-text="langContainer[lang-1]"></span>
            <span v-for="(frame,i2) in frameworkArr" v-text="frameworkContainer[frame-1]"></span>
            <span v-for="(job,i3) in jobArr" v-text="jobContainer[job-1]"></span>
          </div>
        </div>
      </div>
      </router-link>
<!--    </a>-->
    </div>
  </div>

</template>

<script setup>

import {ref, toRef, watchEffect} from 'vue'
import { defineProps } from 'vue';
let p = defineProps( {
  resOne : Object,
});
let pp = toRef(p,"resOne")
const url = ref("/meet/" + pp.value.meetingId)
const title = ref(pp.value.title)

const categoryContainer = ["스터디","프로젝트","기타"]
const statusContainer = ["모집중","모집완료"]
const langContainer = ["Java","Python","C#","C++","JavaScript","Ruby","Swift","TypeScript","PHP"]
const frameworkContainer =  ["Spring","React","Angular","Vue.js","Express.js","Django","Ruby on Rails","Flask","Laravel"]
const jobContainer = ["프론트엔드","백엔드","풀스택","모바일 앱 개발","게임 개발","데이터베이스","데브옵스","디자이너","기획자"]
const category = ref(categoryContainer[pp.value.category])
const status = ref(statusContainer[pp.value.status])
const location = ref(pp.value.location)

const writer = ref(pp.value.userNickname)
const endDate = ref(pp.value.applicationDeadline)
const nowCount = ref(pp.value.applicationCount)
const viewCount = ref(pp.value.views)
const commentCount = ref(pp.value.commentCount)
let langArr = []
let frameworkArr = []
let jobArr = []
langArr = pp.value?.interestLanguage && ref(pp.value.interestLanguage.split("_"));
frameworkArr = pp.value?.interestFramework && ref(pp.value.interestFramework.split("_"))
jobArr = pp.value?.interestJob && ref(pp.value.interestJob.split("_"))
watchEffect(() => {
  url.value = "/meet/" + pp.value.meetingId
  title.value = pp.value.title
  category.value = categoryContainer[pp.value.category]
  status.value = statusContainer[pp.value.status]
  location.value = pp.value.location
  writer.value = pp.value.userNickname
  endDate.value = pp.value.applicationDeadline
  nowCount.value = pp.value.applicationCount
  viewCount.value = pp.value.views
  commentCount.value = pp.value.commentCount
  langArr = pp.value?.interestLanguage && pp.value.interestLanguage.split("_")
  frameworkArr = pp.value?.interestFramework && pp.value.interestFramework.split("_")
  jobArr =pp.value?.interestJob && pp.value.interestJob.split("_")

})



</script>


