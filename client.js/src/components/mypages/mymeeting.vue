<template>
  <div>
    <ul class="mymeeting-list">
      <li class="mymeeting-container" >
        <a class="click-meetingpost" :href="`/meeting/${meetingId}`">
          <div class="meetinginfo">
            <!-- 게시글 항목 status & title-->
            <div class="meeting-title">
              <div class="meeting-status" >
                <div class="meeting-status-text-ing" v-if="status===0">모집중</div>
                <div class="meeting-status-text" v-else-if="status===1">모집완료</div>
              </div>
              <div class="meeting-category">
                <div class="meeting-category-text" v-if="category===0">스터디</div>
                <div class="meeting-category-text" v-else-if="category===1">프로젝트</div>
                <div class="meeting-category-text" v-else-if="category===2">기타</div>
              </div>
              <div class="meeting-title-div">
                <div class="meeting-title-text" v-text="title"></div>
              </div>
            </div>
            <!--게시글 항목 내용-->
            <div class="meeting-body">
              <p class="meeting-body-content" v-text="description"></p>
            </div>
            <!--게시글 항목 하단-->
            <div class="meeting-footer">
              <div class="meeting-cratedata">
                <span class="meeting-footer-text">작성일</span>
                <span class="meeting-footer-content-text" v-text="creationDate"></span>
              </div>
              <div class="meeing-location">
                <span class="meeting-footer-text">장소</span>
                <span class="meeting-footer-content-text" v-text="location"></span>
              </div>
              <div class="meeting-count">
                <span class="meeting-footer-text">신청인원</span>
                <span class="meeting-footer-content-text" v-text="applicationCount+ ` / ` + recruitmentCount"></span>
              </div>
              <div class="meeting-deadline">
                <span class="meeting-footer-text">모집마감일</span>
                <span class="meeting-footer-content-text" v-text="applicationDeadline"></span>
              </div>
              <div class="meeting-view">
                <span class="meeting-footer-text">조회수</span>
                <span class="meeting-footer-content-text" v-text="views"></span>
              </div>
            </div>
          </div>
        </a>
      </li>
    </ul>
  </div>
</template>

<script setup>
import {defineProps, ref, toRef, toRefs, watch, watchEffect} from 'vue';
  const  p = defineProps({
    meetingone : Object
  });
  const pp = toRef(p, "meetingone");
  const {applicationCount, applicationDeadline, category, creationDate, location, meetingId, description, recruitmentCount, status, title, views} = toRefs(p.meetingone);
  watchEffect(()=>{
    title.value = pp.value.title
    category.value = pp.value.category
    status.value = pp.value.status
  })
</script>

<style src="../../assets/css/mymeetinglistView.css" scoped>

</style>