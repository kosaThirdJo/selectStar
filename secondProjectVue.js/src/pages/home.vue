<script setup>
import Card from "../components/element/card.vue";
import {defineProps, ref} from 'vue'
import { api } from "../common.js";

// 최신글 (orderby)
const req = {
  page:0,
  size:4,
  order:"desc",
  criteria:"creationDate"
}
const cardResult = ref([]);
api(
    "meeting?" +
    "page=" + req.page + "&" +
    "size=" + req.size + "&" +
    "order=" + req.order + "&" +  // '=' 추가
    "criteria=" + req.criteria,
    "GET", ""
).then(response => {
  cardResult.value = response.content;
});
defineProps({})

// 인기글 (rank)
const rankResult = ref([]);
api(
    "rankMeeting",
    "GET", ""
).then(response => {
  rankResult.value = response.content;
});

</script>
<template>
  <div class="main-content-container">
<!--    video-->
    <div class="main-video">
      <video autoplay loop muted>
        <source src="../assets/video/coding.mp4" type="video/mp4">
      </video>
    </div>

    <div class="main-content-all">
      <!--    left-->
      <div class="main-content-left">
        <div class="main-content-left-top">
          <div class="main-content-left-title">
            <div class="main-content-left-title-text">
              <img src="../assets/image/global/notifications01.png" style="width: 30px; height: 30px">
              <span class="main-content-left-title-text">ORDER BY</span></div>
            <div class="main-content-right-rank-info">
              <span>모집 중인 최신 모임 글</span>
            </div>
          </div>
        </div>
<!--        최신 모임 글 출력-->
        <div class="main-left-content-bottom">
          <card v-for="(resOne, i) in cardResult" :key="i" :resOne="resOne"></card>
        </div>
      </div>

        <!-- right -->
        <div class="main-content-right"><!-- intro -->
          <div class="main-content-right-intro">
            <div class="main-content-right-intro-textbox">
              <span class="main-content-right-intro-text1"><span>개발자 모임은</span></span>
              <span class="main-content-right-intro-text2"><span>SELECT *에서 찾아보자!</span></span>
            </div>
            <div class="main-content-right-intro-circle"></div>
          </div>
          <!-- rank -->
          <div class="main-content-right-rank">
            <div class="main-content-right-rank-title">
              <div class="main-content-right-rank-title-text">
                <img src="../assets/image/home/fire.png" style="width: 30px; height: 30px">
                <span class="main-content-right-rank-title-text">RANK</span></div>
              <div class="main-content-right-rank-info">
                <span>일주일 동안 게시된 모임에서 조회 수 높은 순위</span>
              </div>
            </div>
            <!-- rank list -->
            <div class="main-content-right-rank-listbox">
              <div v-for="(resOne, i) in rankResult" :key="i" :resOne="resOne" class="main-content-right-rank-list">
                <span class="main-content-right-rank-num" v-text="i + 1"></span>
                <router-link :to="/meeting/+resOne.meetingId" class="main-content-right-rank-text" v-text="resOne.title"></router-link>
              </div>
            </div>
          </div>

            <!-- rank 끝 -->
          </div>
      </div>
    </div>
</template>

<style scoped src="../assets/css/home.css">

</style>