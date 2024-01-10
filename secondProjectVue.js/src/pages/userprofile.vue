<template>
  <div class="frame-container">
    <div class="frame-profile">
      <div class="profile-title">
        <span class="user-nicname-text" v-text="userInfo.nickname" ></span>님의 프로필
      </div>
      <!--사용자 이미지-->
      <div class="frame-user-img">
        <div>
          <img class="profile-img"
               :src="userInfo.profilePhoto"
               alt="Image"/>
        </div>
      </div>
      <!--사용자 정보(nickname, email, 한줄소개)-->
      <div class="frame-profile-info">
        <div class="profile-info-text" v-text="userInfo.nickname"></div>
        <div class="profile-info-text" v-text="userInfo.email"></div>
      </div>
      <div class="frame-profile-oneline">
        <div class="profile-oneline-title">한줄소개</div>
        <p class="profile-oneline-text" v-text="userInfo.aboutMe"></p>
      </div>
    </div>
    <!--사용자 이력 -->
    <div class="frame-profile-content">
      <div class="profile-title">
        <span class="user-nicname-text" v-text="userInfo.nickname"></span>님의 이력
      </div>
      <!--<h2>사용자 이력</h2>-->
      <textarea
          class="profile-info-textarea"
          name="profileContent"
          readonly>{{userInfo.profileContent}}
     </textarea>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {api} from "@/common.js";
import {useRoute} from "vue-router";
import defaultImg from '../assets/image/global/userdefaultimg.png';
const userInfo = ref({});

const route = useRoute();
/*const userInfo = ref({
  "nickname" : "",
  "email" : "",
  "aboutMe" : "",
  "profileContent" : "",
  "profilePhoto" : ""
});*/

onMounted(()=>{
  console.log("User Profile");
  api("profiles/info/"+route.params.user_id, "GET", null)
      .then(res => {
        if(res instanceof Error){
          let errorRes = res;
          console.log(errorRes);
          console.log(errorRes.response);
        }else{
          userInfo.value = res;
          //console.log(userInfo.value);
          if(userInfo.value.profilePhoto === ""){
            userInfo.value.profilePhoto = defaultImg;
          }
          //console.log(userInfo.value);
        }
      });
});

</script>

<style src="../assets/css/userprofile.css" scoped>

</style>