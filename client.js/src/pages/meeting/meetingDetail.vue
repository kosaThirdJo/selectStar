<template>
  <div class="meeting-detail-container">
    <div class="flex-form">
      <div class="frame-writer">
        <div id="profile" class="frame-writer-profile">
          <a class="click-profile" >
            <div class="meeting-articles-profile">
              <router-link :to="'/profiles/info/'+result.userId">
                <img v-if="result.img"
                     class="meeting-articles-profile-photo"
                     :src="result.img"
                     alt="Image"/><br/>
                <img v-if="!result.img" class="meeting-articles-profile-photo" src="@/assets/image/global/userdefaultimg.png" alt="Image">
                <h3 style="color: black" v-text="result.userNickname"></h3>
                <div style="color: black" v-text="result.userAboutMe"></div>
              </router-link>

            </div>
          </a>
        </div>
        <div id="apply" class="frame-showapplicant">
          <span>현재 이 프로젝트에서 <span style="color: #FF9F29; font-weight: 800;" v-text="result.countApplyUsers"></span></span><span>명이 참여중입니다.</span>
        </div>
      </div>
      <div v-if="!isLoading" id="content" class="border border-dark">

        <section id="content_box">
          <div class="title-box">
            <span class="btn" :class="{'btn-silver': result.status}" v-text="(result.status===0) ? '모집 중': '모집 종료'"></span>
            <h2 v-text="result.title" id="content_title" style="display: inline"> </h2>
            <div class="float-right">
              <button v-if="viewBtnApply&&(result.status===0)" class="btn btn-primary" id="apply-modal" @click="showModal = true">신청 하기</button>
              <router-link v-if="viewBtnFix&&(result.status===0)" :to="'/meet/fix/'+route.params.post_id" class="btn btn-primary" ><span>수정 하기
      </span></router-link>
              <Teleport to="body">
                <!-- use the modal component, pass in the prop -->
                <modal :show="showModal" @close="showModal = false" :meeting-id="parseInt(route.params.post_id)">
                  <template #header>
                    <h3>신청 하기</h3>
                  </template>
                </modal>
              </Teleport>

              <button v-if="viewBtnNowApplyInfo&&(result.status===0)" class="btn btn-primary" id="my-apply-modal" @click="showValidModal = true">나의 신청 현황</button>
              <Teleport to="body">
                <!-- use the modal component, pass in the prop -->
                <apply-valid-modal :show="showValidModal" @close="showValidModal = false" :meeting-id="parseInt(route.params.post_id)" >
                  <template #header>
                    <h3>나의 신청 현황</h3>
                  </template>
                </apply-valid-modal>
              </Teleport>
              <button v-if="viewBtnRemoveMeeting&&(result.status===0)" class="btn btn-primary" @click="removeMeeting()">삭제</button>
              <button v-if="viewBtnApplyCompleting&&(result.status===0)" class="btn btn-primary" @click="completeMeeting()">모집 완료</button>
              <button class="btn btn-primary" id="apply-users" @click="applicationStatus = true" v-if="viewBtnApplyList&&(result.status===0)">신청한 사람</button>
              <Teleport to="body">
                <!-- use the modal component, pass in the prop -->
                <apply-reason :show="applicationStatus" @close="applicationStatus = false" :meeting-id="parseInt(route.params.post_id)" >
                  <template #header>
                    <h3>신청한 사람</h3>
                  </template>
                </apply-reason>
              </Teleport>
            </div>
          </div>

          <div class="view-box">
            <span></span>
            <span style="font-weight: bold"> 조회수 </span>
            <span v-text="result.views"></span>
          </div>
          <div v-text="result.description" id="content_description" class="main-content-box">
          </div>
          <div id="end_date_line">
            <span style="font-weight: bold">마감일 </span>
            <span v-text="result.applicationDeadline"></span>
          </div>
          <div id="location">
            <span style="font-weight: bold"> 위치 </span>
            <span v-text="result.location"></span>
          </div>
        </section>
        <section id="comment_box">
          <div><span style="font-weight: bold">댓글 </span><span v-text="commentResult.length" style="color: #1A4D2E"></span>
          </div>
          <div id="comment_input_line">
            <input id="comment_input" v-model="commentInput" @keyup.enter="writeComment()" class="mt-2 mb-2" type="text" name="commentContent" placeholder=" 댓글을 작성해 보세요">
            <span id="comment_button" class="btn btn-primary mr-3" style="width: 55px;" @click="writeComment()">등록</span>
          </div>
          <div class="main-content-container">
            <div class="comment_list" v-for="(commentEle,commentIdx) in commentResult">
              <div id="comment_title" v-text="commentEle.userNickName"></div>
              <div v-text="commentEle.content"></div>
              <div v-text="commentEle.creationDate"></div>
            </div>
          </div>
        </section>
      </div>
      <div v-else>
        <div>
          로딩 중...
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>

import { ref, watch } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {api, apiToken} from "@/common.js";
import Modal from '../../components/meeting/applyModal.vue'
import ApplyValidModal from "@/components/meeting/myApplyModal.vue";
import ApplyReason from "@/components/meeting/applyReasonModal.vue";


const viewBtnApplyCompleting = ref(false)
const viewBtnRemoveMeeting = ref(false)
const viewBtnApplyList = ref(false)
const viewBtnFix = ref(false)
const viewBtnApply = ref(false)
const viewBtnNowApplyInfo = ref(false)
const showModal = ref(false)
const showValidModal = ref(false)
const applicationStatus = ref(false)
const route = useRoute()
const router = useRouter()
const isLoading = ref(true);
const result = ref([])
const commentResult = ref([])


apiToken(
    "meeting/" +
    route.params.post_id,
    "GET", null,
    localStorage.getItem("jwtToken")
).then(response => {
  result.value = response
  console.log(result)
  isLoading.value = false;
  if (!localStorage.getItem("jwtToken")){
    return
  }
  // 로그인아이디와 작성자가 다를경우
  if (response.loginId !== response.userId){
    // 신청 테이블 조회
    apiToken(
        "apply/check?meetingId=" + route.params.post_id,
        "GET",
        "",
        localStorage.getItem("jwtToken")
    ).catch(e => {
      console.log(e)
    }).then(response1 => {
          console.log(response1)
      if (response1){
        viewBtnNowApplyInfo.value = true
      } else {
        viewBtnApply.value = true
      }
        })


    return;
  }
  // 자기가 작성자인 경우
  viewBtnRemoveMeeting.value = true
  viewBtnApplyList.value = true
  viewBtnFix.value = true
  viewBtnApplyCompleting.value = true
  console.log(response)

});
api(
    "comment/meeting/" +
    route.params.post_id,
    "GET", ""
).then(response => {
  commentResult.value = response.content
});
const commentInput = ref("")



function completeMeeting(){
  const result = confirm("모집 완료 하실껀가요?");
  if(result) {
  } else {
    alert("모집 완료 취소되었습니다.");
    return;
  }


  apiToken(
      "meeting",
      "PATCH", {
        "meetingId":route.params.post_id},
      localStorage.getItem("jwtToken")
  ).catch(() => {
        alert("모집 완료 실패")
        return
      }
  ).then(() => {
        alert("모집 완료 되었습니다.")
        router.go(-1)
      }
  )
}
function removeMeeting() {
  const result = confirm("삭제 하실껀가요?");
  if(result) {
  } else {
    alert("삭제 취소되었습니다.");
    return;
  }

  apiToken(
      "meeting/" + route.params.post_id,
      "DELETE",
      "",
      localStorage.getItem("jwtToken")
  ).catch( () => {
        alert("삭제 실패")
    return
      }
  ).then( () => {
        alert("삭제 되었습니다.")
        router.go(-1)
      }
  )

}
function writeComment(){
  if (!localStorage.getItem("jwtToken")){
    alert("로그인을 해 주세요")
    router.replace("/login")
    return ;
  }
  if (!commentInput.value){
    alert("내용을 입력 해 주세요")
    router.go(0)
    return;
  }
  apiToken("comment/meeting/" + route.params.post_id,
      "POST",
      {
        meetingId:route.params.post_id,
        content: commentInput.value,
      },
      localStorage.getItem("jwtToken")
  )
  router.go(0)
}


</script>



<style scoped>
  section{
    text-align: left;
    margin: 10px;
  }

  #comment_button{
    float: right;
  }
  #comment_input{
    width: 90%;
  }
  .comment_list{
    background-color: antiquewhite;
    margin-bottom: 10px;
  }
.meeting-detail-container{
  min-height: 1300px;
}
  .flex-form{
    margin-top: 20px;
    display: flex;
    flex-direction: row-reverse;
    justify-content: center;
    gap: 50px;
  }
  .meeting-articles-profile-photo{
    width:5rem; height:5rem; border-radius:9999px;
  }
  .frame-writer {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    margin-left: 30px;
  }
  .meeting-articles-profile{

    padding-top: 20px;
    text-align: center;
    gap: 5px;
  }
  .meeting-articles-profile-photo{
    width:5rem;
    height:5rem;
    border-radius:9999px;
    text-align: center;
    margin: auto;
  }

  #content_description{
    margin-top: 20px;
    margin-bottom: 20px;
    min-height: 500px;
  }

  .sub-info{
    margin: 20px 0 20px;
  }
  .frame-writer-profile {
    float: right;
    background-color: #FAF3E3;
    width: 330px;
    height: 350px;
    display: flex;
    flex-direction: column;
    margin-right: 50px;
    margin-top: 50px;
    border: none;
    border-radius: 20%;
  }
  .frame-showapplicant{
    float: right;
    background-color: white;
    width: 300px;
    text-align: center;
    margin-top: 20px;
  }

  #content_box {
    width: 800px;
    padding: 5px 5px 5px 5px;
  }

  #content_title {
    margin-top: 20px;
    margin-bottom: 20px;
  }

  #comment_box {
    background-color: #FAF3E3;
    width: 800px;
    padding: 20px 20px 20px 20px;
  }

  .comment{
    padding-bottom: 5px;
  }
  .comment-content{
    padding-bottom: 10px;
  }

  #comment_input_line {
    margin: 5px 5px 50px;
    display: flex;
    gap: 0px 18px;
  }
  #modal-button{
    float: right;
  }

  #comment-input {
    display: flex;
    outline: none;
    border-radius: 10px;
    border: none;
    margin: 20px 0 20px 0;
  }
  .modal-input{
    border-radius: 5px;
    border: #888888;
    margin: 5px 0 10px 0;
  }
  .input-border-radius{
    outline: none;
    border-radius: 10px;
    border: none;
  }

  #comment_list {
    padding-top: 10px;
    padding-left: 10px;
    background-color: white;
    margin-bottom: 5px;
    margin-top: 10px;
  }
  .title-box{
    margin-bottom: 15px;
  }

  #comment_title {
    margin-top: 5px;
    margin-left: 5px;
  }

  article {
    margin-left: 200px;
    margin-top:100px;
    min-height: 1000px;
  }
  .click-profile{
    text-decoration: none;
    color: black;
    margin: 10px 0px;
  }
  .meeting-articles-profile {
    display: flex;
    text-align: center;
    gap: 10px;
    flex-direction: column;
  }
  .meeting-articles-profile-photo {
    width: 10rem;
    height: 10rem;
    border-radius: 9999px;
    text-align: center;
    margin: auto;
  }
  .writer-nickname-text{
    font-size: 18px;
    font-weight: 700;
  }
  .btn{
    background-color: #FF9F29;
    border: white;
    color: white;
    margin-right: 10px;
  }
  .btn:hover{
    cursor: default; /* 기본 커서로 변경 */
  }
  .btn-silver{
    background-color: grey;
    color: white

  }


</style>
<!--
<style src="../css/meeting/meeting_home.css" scoped>
<style src="../assets/css/meeting/meeting_home.css" scoped>
</style>
<style src="../../assets/css/meeting/meeting_article.css" scoped>
</style>
<style src="../../assets/css/home.css" scoped>
</style>
<style src="../css/home.css" scoped>
</style>-->
