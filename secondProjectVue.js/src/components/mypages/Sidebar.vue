<template>
  <aside>
    <div class="frame-aside">
      <div class="frame-profileinfo">
        <div class="frame-profile">
          <img
              class="profile-img"
                :src="previewPhoto || myInfo.profilePhoto"
               alt="Image"/><br/>
          <span class="text-nickname" v-text="myInfo.nickname"></span>
          <span class="text-email" v-text="myInfo.email"></span>
          <div class="frame-img-button">
            <label class="input-file-button" for="inputImgFile">사진 변경</label>
            <input
                type="file"
                id="inputImgFile"
                ref="imgfileInput"
                name="profilePhoto"
                accept=".jpg, .png"
                @change="previewImg"
                required hidden/>
            <input
                id="updateimg"
                class="input-submit"
                type="button"
                @click="updateProfileImg()"
                value="변경 저장"/>
          </div>
        </div>
      </div>
      <div class="frame-sidemenu">
        <div class="frame-sidemenu-home">
          <span class="frame-sidemenu-title">내 정보 관리</span>
          <div>
            <ul>
              <li class="side-menu-li">
                <router-link class="sidemenu-a" to="/users/myprofile">이력 관리</router-link>
              </li>
              <li class="side-menu-li">
                <router-link class="sidemenu-a" to="/users/myinfo">개인 정보 수정</router-link>
              </li>
            </ul>
          </div>
        </div>
        <div class="frame-sidemenu-meeting">
          <span class="frame-sidemenu-title">모임</span>
          <div>
            <ul>
              <li class="side-menu-li">
                <router-link class="sidemenu-a" to="/users/mymeeting">내가 만든 모임</router-link>
              </li>
              <li class="side-menu-li">
                <router-link class="sidemenu-a" to="/users/myapplying">내가 참여한 모임</router-link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {useRoute} from "vue-router";
import {api} from "../../common.js";
import defaultImg from '@/assets/image/global/userdefaultimg.png';

const route = useRoute();//CompositionAPI 매칭된 라우트 (OptionAPI : this.$route)
const getDataErr = reactive({});
const myInfo = ref({});
//[이미지 수정]input태그에서 file값 받는 변수
const imgfileInput = ref(null);
//[이미지 미리보기]
const previewPhoto = ref(null);
const token = localStorage.getItem("jwtToken");

//프로필 정보 받아서 파싱
async function getData(){
  try {
    const response1 = (await axios.get("http://43.201.149.206:8081/users/profile", {
      headers : {
        Authorization: token
      }
    }));
    myInfo.value = response1.data;
    if(myInfo.value.profilePhoto === ""){
      myInfo.value.profilePhoto = defaultImg;
    }
    console.log(myInfo.value);
  }catch (error){
    getDataErr.value =error;
    //console.log(getDataErr.value);
  }
}
onMounted(()=>{
  console.log("sidebar onmount");
  getData();
});

//이미지 미리보기 기능
const previewImg = (event) => {
  let inputFile = event.target;
  let reader = new FileReader();

  reader.onload = () => {
    previewPhoto.value = reader.result;
    console.log(myInfo.previewPhoto);
    document.getElementById('updateimg').style.display='block';
  };
  reader.readAsDataURL(inputFile.files[0]);
  document.getElementById("")
};

//이미지 수정 기능
function updateProfileImg(){
  if(!confirm("정말 수정하시겠습니까?")) {
      alert("취소되었습니다.");
      window.location.reload();
   }else {
      //multipart/form-data 형식으로 전송
     let fd = new FormData();
     fd.append("profilePhoto", imgfileInput.value.files[0]);
     axios.put("http://43.201.149.206:8081/users/setting/img", fd, {
       headers: {
         'Content-Type' : 'multipart/form-data',
         'Authorization' : token
       }
     }).then(res => {
           console.log(res);
           window.location.reload();
         }).then(error => {
       console.log(error);
     });
     console.log("수정완료");
   }
  }
</script>

<style src="../../assets/css/sidebar.css" scoped>

</style>