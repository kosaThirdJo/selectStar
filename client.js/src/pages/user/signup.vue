<style src="@/assets/css/signup.css">
.green{
  color: green;
}
</style>

<template>
  <div class="signup-container">
    <div class="signup-content">
      <!--      01 상단 텍스트 : 로고, 설명 -->
      <div class="signup-top">
        <router-link class="signup-top-logo" to="/">
          <span class="signup-top-logo-text">
              SELECT <span class="signup-top-logo-text-star">*</span>
          </span>
        </router-link>
        <span class="signup-top-intro1">
          <span>SELECT*에 오신것을 환영합니다.</span>
        </span>
        <span class="signup-top-intro2">
          <span>SELECT*는 개발자를 위한 모임 커뮤니티입니다.</span>
        </span>
      </div>

      <!--       02 입력 폼-->
      <div class="signup-form">
        <div class="signup-form-input">
          <!--  02-02-01 아이디  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title">아이디</span>
              <input
                  v-model="signupInfo.name"
                  class="signup-form-input-box-content"
                  name="name"
                  placeholder="4~15자 이내로 입력해주세요."
                  type="text"
                  @input="validateUsername"
              />
            <div class="sign-up-form-input-check-alert" :style="{color: check.name ? 'green' : 'red'}">{{ msg.name }}</div>
          </div>

          <!--  02-02-02 비밀번호  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title">비밀번호</span>
            <input
                v-model="signupInfo.password"
                class="signup-form-input-box-content"
                name="password"
                placeholder="최소 6자 이상, 알파벳과 숫자로 입력해주세요."
                type="password"
                @input="validatePassword"
            />
            <div class="sign-up-form-input-check-alert">{{ msg.password }}</div>
          </div>

          <!--  02-02-03 이메일  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title">이메일</span>
            <input
                v-model="signupInfo.email"
                class="signup-form-input-box-content"
                name="email"
                placeholder="honggildong@select.com"
                type="email"
                @input="validateEmail"
            />
            <div class="sign-up-form-input-check-alert">{{ msg.email }}</div>
          </div>

          <!--  02-02-04 닉네임  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title"><span>닉네임</span></span>
              <input
                  v-model="signupInfo.nickname"
                  class="signup-form-input-box-content"
                  name="nickname"
                  placeholder="닉네임을 20자 이하로 입력해주세요."
                  type="text"
                  @input="validateNickname"
              />
            <div class="sign-up-form-input-check-alert" :style="{color: check.nickname ? 'green' : 'red'}" >{{ msg.nickname }}</div>
          </div>

          <!--  02-02-05 지역  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title"><span>지역</span></span>
            <div class="signup-form-input-withbtn">
              <input
                  id="locationInput"
                  v-model="signupInfo.location1"
                  class="signup-form-input-box-content-withbtn"
                  name="location1"
                  placeholder="현재 지역을 인증해주세요."
                  readonly
                  type="text"
              />
              <button class="signup-form-input-location-btn" type="button" @click="getLocation">지역 인증</button>
            </div>
            <div class="sign-up-form-input-check-alert" :style="{color: check.location ? 'green' : 'red'}">{{ msg.location }}</div>
          </div>

          <!--  02-02-06 관심 분야  -->
          <div class="signup-form-input-box">
            <span class="signup-form-input-box-title"><span>관심 분야</span></span>
            <div class="signup-form-input-box-content" style="padding-bottom: 10px;">
              <!--  02-02-06-01 언어  -->
              <div class="signup-form-select-interest">
                <span class="signup-form-select-interest-title">Language</span>
                <div class="signup-form-select-interest-button">
                  <input id="selected-interests-lang" name="interest_language" type="hidden" value="">
                  <button class="signup-interest-lang-btn" type="button" value="1">Java</button>
                  <button class="signup-interest-lang-btn" type="button" value="2">Python</button>
                  <button class="signup-interest-lang-btn" type="button" value="3">C#</button>
                  <button class="signup-interest-lang-btn" type="button" value="4">C++</button>
                  <button class="signup-interest-lang-btn" type="button" value="5">JavaScript</button>
                  <button class="signup-interest-lang-btn" type="button" value="6">Ruby</button>
                  <button class="signup-interest-lang-btn" type="button" value="7">Swift</button>
                  <button class="signup-interest-lang-btn" type="button" value="8">TypeScript</button>
                  <button class="signup-interest-lang-btn" type="button" value="9">PHP</button>
                </div>
              </div>

              <!--  02-02-06-02 프레임워크  -->
              <div class="signup-form-select-interest">
                <span class="signup-form-select-interest-title">Framework</span>
                <div class="signup-form-select-interest-button">
                  <input id="selected-interests-fw" name="interest_framework" type="hidden" value="">
                  <button class="signup-interest-fw-btn" type="button" value="1">Spring</button>
                  <button class="signup-interest-fw-btn" type="button" value="2">React</button>
                  <button class="signup-interest-fw-btn" type="button" value="3">Angular</button>
                  <button class="signup-interest-fw-btn" type="button" value="4">Vue.js</button>
                  <button class="signup-interest-fw-btn" type="button" value="5">Express.js</button>
                  <button class="signup-interest-fw-btn" type="button" value="6">Django</button>
                  <button class="signup-interest-fw-btn" type="button" value="7">Ruby on Rails</button>
                  <button class="signup-interest-fw-btn" type="button" value="8">Flask</button>
                  <button class="signup-interest-fw-btn" type="button" value="9">Laravel</button>
                </div>
              </div>

              <!--  02-02-06-01 직무  -->
              <div class="signup-form-select-interest">
                <span class="signup-form-select-interest-title">Role</span>
                <div class="signup-form-select-interest-button">
                  <input id="selected-interests-job" name="interest_job" type="hidden" value="">
                  <button class="signup-interest-job-btn" type="button" value="1">프론트엔드</button>
                  <button class="signup-interest-job-btn" type="button" value="2">백엔드</button>
                  <button class="signup-interest-job-btn" type="button" value="3">풀스택</button>
                  <button class="signup-interest-job-btn" type="button" value="4">모바일 앱 개발</button>
                  <button class="signup-interest-job-btn" type="button" value="5">게임 개발</button>
                  <button class="signup-interest-job-btn" type="button" value="6">데이터베이스</button>
                  <button class="signup-interest-job-btn" type="button" value="7">데브옵스</button>
                  <button class="signup-interest-job-btn" type="button" value="8">디자이너</button>
                  <button class="signup-interest-job-btn" type="button" value="9">기획자</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--  02-02-08 회원 가입 버튼  -->
        <div class="signup-signupbtn"><a href="#" @click="signup()"></a></div>

        <!--03 로그인 페이지 이동-->
        <div class="signup-movetoLogin">
          <span>이미 회원이신가요?</span>
          <router-link class="signup-movetoLogin-loginPg" to="/login">로그인</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import axios from "axios";
import {api} from "@/common.js";
import router from "@/router/index.js";

// 회원가입 폼
const signupInfo = ref({
  name: '',
  password: '',
  email: '',
  nickname: '',
  location1: '',
  interestLanguage: null,
  interestFramework: null,
  interestJob: null,
});

// message
    const msg = ref({
      name: '',
      password: '',
      email: '',
      nickname: '',
      location: '',
    })

// 유효성 검사 조건
    const usernameRegex = /^[a-zA-Z0-9]{4,15}$/;
    const passwordRegex = /^[a-zA-Z0-9]{6,}$/;
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,20}$/;

// 검사 여부 확인
    const check = reactive({
      name: false,
      password: false,
      email: false,
      nickname: false,
      location: false,
    });

// 유효성 검사 & 중복 확인
    const validateUsername = async () => {
      const value = signupInfo.value.name;
      if (!signupInfo.value.name || !usernameRegex.test(signupInfo.value.name)) {
        check.name = false;
        msg.value.name = "아이디를 4자 이상, 15자 이하의 알파벳과 숫자로 입력해주세요.";
      } else {
        const response = await api(`users/checkDuplicate?type=name&value=${value}`, "GET");
        if (response instanceof Error) {
          msg.value.name = response.response.data;  // 이미 존재하는 아이디입니다.
          check.name = false;
        } else {
          msg.value.name = response;  // 사용 가능한 아이디입니다.
          check.name = true;
        }
      }
    };

    const validatePassword = () => {
      if (!signupInfo.value.password || !passwordRegex.test(signupInfo.value.password)) {
        check.password = false;
        msg.value.password = "비밀번호는 최소 6자 이상, 알파벳과 숫자로 입력해주세요.";
      } else {
        check.password = true;
        msg.value.password = "";
      }
    };
    const validateEmail = () => {
      if (!signupInfo.value.email || !emailRegex.test(signupInfo.value.email)) {
        check.email = false;
        msg.value.email = "올바른 이메일 주소를 입력해주세요.";
      } else {
        check.email = true;
        msg.value.email = "";
      }
    };
    const validateNickname = async () => {
      const value = signupInfo.value.nickname;
      if (!signupInfo.value.nickname || !nicknameRegex.test(signupInfo.value.nickname)) {
        check.nickname = false;
        msg.value.nickname = "닉네임는 2자 이상, 20자 이하로 입력해주세요.";
      } else {
        const response = await api(`users/checkDuplicate?type=nickname&value=${value}`, "GET");
        if (response instanceof Error) {
          check.nickname = false;  // 이미 존재하는 닉네임입니다.
          msg.value.nickname = response.response.data;
        } else {
          check.nickname = true;
          msg.value.nickname = response;  // 사용 가능한 닉네임입니다.
        }
      }
    };

    watch(() => signupInfo.name, validateUsername);
    watch(() => signupInfo.password, validatePassword);
    watch(() => signupInfo.email, validateEmail);
    watch(() => signupInfo.nickname, validateNickname);

// 지역 인증
    const getLocation = async () => {
      // apiKey 가져오기
      api("users/apiKey", "GET")
          .then(response => {
            const apiKey = response;
            // geolocation
            if (navigator.geolocation) {
              navigator.geolocation.getCurrentPosition(async (position) => {
                const x = position.coords.longitude;
                const y = position.coords.latitude;
                if (x && y) {
                  // kakaoapi
                  const response = await axios.get(
                      `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${x}&y=${y}`,
                      {headers: {Authorization: `KakaoAK ${apiKey}`}}
                  );
                  signupInfo.value.location1 = response.data.documents[0].region_1depth_name;
                  msg.value.location = "현재 지역이 인증되었습니다.";
                  check.location = true;
                }
              });
            } else {
              alert('브라우저가 위치 정보를 지원하지 않습니다.');
            }
          });
    };

// 관심분야
    onMounted(() => {
      // 언어
      const interestLangButtons = document.querySelectorAll('.signup-interest-lang-btn');
      const selectedInterestsLangInput = document.getElementById('selected-interests-lang');
      let selectedInterestsLang = [];
      selectInterests(interestLangButtons, selectedInterestsLang, selectedInterestsLangInput,"lang");
      // 프레임 워크
      const interestFwButtons = document.querySelectorAll('.signup-interest-fw-btn');
      const selectedInterestsFwInput = document.getElementById('selected-interests-fw');
      let selectedInterestsFw = [];
      selectInterests(interestFwButtons, selectedInterestsFw, selectedInterestsFwInput,"fw");
      // 직무
      const interestJobButtons = document.querySelectorAll('.signup-interest-job-btn');
      const selectedInterestsJobInput = document.getElementById('selected-interests-job');
      let selectedInterestsJob = [];
      selectInterests(interestJobButtons, selectedInterestsJob, selectedInterestsJobInput,"job");

      let langArr = []
      let langs = langArr.filter((element) => element !== "");
      //프레임워크
      let fwArr = []
      let fws = fwArr.filter((element) => element !== "");
      //직무
      let jobArr = []
      let jobs = jobArr.filter((element) => element !== "");

      window.onload = function(){
        //관심 언어
        let langButtons = document.querySelectorAll('.signup-interest-lang-btn');
        for(let lang of langs){
          for(let langBtn of langButtons){
            if(lang === langBtn.value){
              langBtn.className = "signup-interest-lang-btn selected";
              selectedInterestsLang.push(langBtn.value);
            }
          }
        }
        //관심 프레임워크
        let fwButtons = document.querySelectorAll('.signup-interest-fw-btn');
        for(let fw of fws){
          for(let fwBtn of fwButtons){
            if(fw === fwBtn.value){
              fwBtn.className = "signup-interest-fw-btn selected";
              selectedInterestsFw.push(fwBtn.value);
            }
          }
        }
        //관심 직무
        let jobButtons = document.querySelectorAll('.signup-interest-job-btn');
        for(let job of jobs){
          for(let jobBtn of jobButtons){
            if(job === jobBtn.value){
              jobBtn.className ="signup-interest-job-btn selected";
              selectedInterestsJob.push(jobBtn.value);
            }
          }
        }
      }
      // 관심 분야 선택
      // 버튼 목록, 선택된 관심사 배열, hidden input에 저장
      function selectInterests(buttons, selectedInterests, selectedInterestsInput, type) {
        buttons.forEach(button => {
          button.addEventListener('click', () => {  // click될 때마다 실행
            const interest = button.value;  // 클릭된 버튼 value

            if (selectedInterests.includes(interest)) {  // 선택되어있으면, 배열에서 제거
              selectedInterests = selectedInterests.filter(item => item !== interest);
            } else {  // 선택되어있지않으면, 배열에 추가
              selectedInterests.push(interest);
            }
            selectedInterestsInput.value = selectedInterests.join('_');
            button.classList.toggle('selected');  // 버튼 상태 토글
            switch (type){
              case "lang":
                signupInfo.value.interestLanguage = selectedInterestsInput.value
                break
              case "fw":
                signupInfo.value.interestFramework = selectedInterestsInput.value
                break
              case "job":
                signupInfo.value.interestJob = selectedInterestsInput.value
            }
          });
        });
      }
    })

// 회원가입 폼 제출
async function signup() {
  // 유효성 검사 & 중복 확인
  await validateUsername();
  validatePassword();
  validateEmail();
  await validateNickname();
  if (!check.location) {
    msg.value.location = "지역을 인증해주세요.";
  } else {
    if (check.name && check.password && check.email && check.nickname && check.location) {
      const response = await api("users", "POST", signupInfo.value);
      if (response) {
        if (confirm("회원가입에 성공했습니다.\n로그인 페이지로 이동 하시겠습니까?")) {
          router.replace("/login");
        } else {
          router.replace("/");
        }
      }
    }
  }


}
</script>