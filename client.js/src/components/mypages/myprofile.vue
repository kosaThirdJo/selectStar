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
import {api, apiToken} from "../../common.js";
import Sidebar from "./Sidebar.vue";
const router = useRouter();
function getCurrentURL() {
  const currentURL = window.location.href;
  router.push({ path:currentURL });
}

const getDataErr = reactive({});
const myInfo = ref({
  "userId":"",
  "aboutMe" : "",
  "profileContent" : ""
});
const token = localStorage.getItem("jwtToken");

function updateData(){
  if(!confirm("정말 수정하시겠습니까?")) {
    alert("취소되었습니다.");
    window.location.reload();
    //router.push({ path: '/' }); // 현재 경로로 리디렉션
    //router.go(0); //404에러
    //getCurrentURL();
    //router.push({ path: '/users/myprofile' });
  }else {
    apiToken(
        "users/profile", "PATCH",
        {
          aboutMe: myInfo.value.aboutMe,
          profileContent: myInfo.value.profileContent
        }, token)
        .then(response2 => {
          if (response2 instanceof Error) {
          } else {
            alert("수정완료 되었습니다.");
            window.location.reload();
            //router.push({ path: '/' }); // 현재 경로로 리디렉션
            //router.go(0); //404에러
            //getCurrentURL();
            //router.push({ path: '/users/myprofile' });
          }
        });
  }
}
async function getData(){
  try {
    const response1 = (await axios.get("http://43.201.149.206:8081/users/profile", {
      headers : {
        Authorization: token
      }
    }));
    myInfo.value = response1.data;
    // console.log(myInfo.value);
  }catch (error){
    getDataErr.value =error;
    //console.log(getDataErr.value);
  }
}
onMounted(()=>{
  getData();
});
</script>

<style src="../../assets/css/myprofile.css" scoped>
</style>