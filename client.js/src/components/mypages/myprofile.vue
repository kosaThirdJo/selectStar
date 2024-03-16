<template>
  <section class="frame-content">
    <div class="frame-content-title">
      <div class="content-title">
        <span class="content-title-text">이력 관리</span>
      </div>
    </div>
    <div class="frame-content-body">
      <input type="hidden" name="userId" v-model="myInfo.userId"/>
      <!-- 한줄 소개 -->
      <div class="frame-oneline">
        <span class="frame-oneline-titletext">나의 소개(한줄 소개)</span>
        <input
            class="input-oneline"
            type="text"
            name="aboutMe"
            v-model="myInfo.aboutMe"
            maxlength="30"
            placeholder="한줄로 간단하게 나를 소개해주세요" />
      </div>
      <div class="msgAboutme" v-if="myInfo.aboutMe&&myInfo.aboutMe.length>=30">30자 이내로 작성해주세요.</div>
      <!--이력관리-->
      <div class="frame-profilecontent">
        <span class="frame-profilecontent-titletext">이력관리</span>
        <textarea
            class="input-profilecontent"
            name="profileContent"
            v-model="myInfo.profileContent"
            placeholder="이력을 상세히 작성해주세요.&#13;&#10;작성예시&#13;&#10자격증/수료한 교육/프로젝트 경험">
          </textarea>
        <div class="frame-profile-textcnt">
          <span>글자수&nbsp;: &nbsp;</span>
          <span class="frame-profile-textcnt-cnt">{{myInfo.profileContent? myInfo.profileContent.length : 0}}</span>
        </div>
      </div>
        <div class="frame-profile-file">
            <input
                type="file"
                name="profileFile"
                @change="handleFileUpload($event)"/>
        </div>
      <div class="frame-bottom">
        <input @click="updateData()" id="submitbutton" class="button-submit"  value="수정하기"/>
      </div>
    </div>
  </section>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import {useRoute, useRouter} from "vue-router";
import {api, apiToken, apiToken2, apiTokenMpt} from "../../common.js";
import Sidebar from "./Sidebar.vue";
const router = useRouter();

const myInfo = ref({
    "userId":"",
    "aboutMe" : "",
    "profileContent" : "",
    "profileFile": null
});
const token = localStorage.getItem("jwtToken");

//파일 업로드 처리
function handleFileUpload(event){
    const myFile = event.target.files[0];
    console.log("myFile", myFile);
    myInfo.value.profileFile = myFile;
}

function updateData(){
  if(!confirm("정말 수정하시겠습니까?")) {
    alert("취소되었습니다.");
    window.location.reload();
  }else {
      let formData = new FormData();
      formData.append("aboutMe", myInfo.value.aboutMe);
      formData.append("profileContent", myInfo.value.profileContent);
      if(myInfo.value.profileFile){ //파일이 존재할 경우에만
          formData.append("profileFile", myInfo.value.profileFile);
      }
      apiTokenMpt("users/profile", "PUT", formData, token)
          .then(response2 => {
              console.log(response2);
              if (response2.status === 205) {
                  alert("수정완료되었습니다.");
                  window.location.reload();
              } else {
                  console.log(response2);
              }
          });
  }
}
async function getData(){
    apiToken2("users/profile", "GET", null, token)
        .then(async response => {
            console.log(response.data);
            myInfo.value = response.data;
    });
}
onMounted(()=>{
  getData();
});
</script>

<style src="../../assets/css/myprofile.css" scoped>
</style>