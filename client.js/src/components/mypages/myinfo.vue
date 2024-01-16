<template>
  <section>
    <div class="myinfoframe-content-title">
      <div class="myinfocontent-title">
        <span class="myinfocontent-title-text">개인정보 수정</span>
      </div>
    </div>
    <div class="myinfo-content">
      <div id="updateInfoForm">
        <input type="hidden" name="userId"/>
        <div class="myinfo-input">
          <!--  01-01 비밀번호  -->
          <div class="myinfo-password">
            <span class="myinfo-text017"><span>비밀번호</span></span>
            <input
                v-model="myInfo.password"
                type="password"
                name="password"
                placeholder="최소 6자 이상, 알파벳과 숫자로 입력해주세요."
                class="myinfo-input2"
                minlength="6"
                @input="validatePassword"
                required />
            <div class="myinfo-form-input-check-alert">{{ msg.password }}</div>
          </div>
          <!--  01-02 이메일  -->
          <div class="myinfo-email">
            <span class="myinfo-text017"><span>이메일</span></span>
            <input
                type="email"
                name="email"
                v-model="myInfo.email"
                placeholder="honggildong@select.com"
                class="myinfo-input3"
                @input="validateEmail"/>
            <div class="myinfo-form-input-check-alert">{{ msg.email }}</div>
          </div>
          <!--  01-03 닉네임  -->
          <div class="myinfo-nickname">
            <span class="myinfo-text017"><span>닉네임</span></span>
            <div class="myinfo-nickname-content">
              <input
                  id="inputNickname"
                  type="text"
                  name="nickname"
                  v-model="myInfo.nickname"
                  placeholder="닉네임을 20자 이하로 입력해주세요."
                  maxlength="20"
                  class="myinfo-input4"
                  @input="validateNickname"
                  required />
              <div class="myinfo-form-input-check-alert" :style="{color: check.nickname ? 'green' : 'red'}" >{{ msg.nickname }}</div>
            </div>
          </div>
          <div id="showResult"></div>
          <!--  01-04 지역1 -->
          <div class="myinfo-location">
            <span class="myinfo-text017"><span>지역1</span></span>
            <div class="myinfo-location-content">
              <input
                  v-model="myInfo.location1"
                  type="text" name="location1" placeholder="현재 지역을 인증해주세요."
                  class="myinfo-input5" id="locationInput1" readonly />
              <button @click.prevent="getLocation" class="myinfo-location1btnbutton">
                <span class="myinfo-text025"><span>지역 인증</span></span>
              </button>
              <div class="myinfo-form-input-check-alert" id="checkLocation">
                <span></span>
              </div>
            </div>
          </div>
          <!--  01-05 지역 2  -->
          <div class="myinfo-location">
            <span class="myinfo-text017"><span>지역2</span></span>
            <div class="myinfo-location-content">
              <input
                  v-model="myInfo.location2"
                  type="text" name="location2" placeholder="현재 지역을 인증해주세요."
                  class="myinfo-input5" id="locationInput2" readonly />
              <button @click.prevent="getLocation2" class="myinfo-location2btnbutton">
                <span class="myinfo-text025"><span>지역 인증</span></span>
              </button>
              <div class="myinfo-form-input-check-alert" id="checkLocation2">
                <span></span>
              </div>
            </div>
          </div>
          <!--  01-06 관심 분야  -->
          <div class="myinfo-form-input-box">
            <span class="myinfo-form-input-box-title"><span>관심 분야</span></span>
            <div class="myinfo-form-input-box-content" style="padding-bottom: 10px;">
              <!--  01-06-01 언어  -->
              <div class="myinfo-form-select-interest">
                <span class="myinfo-form-select-interest-title">Language</span>
                <div class="myinfo-form-select-interest-button">
                  <input type="hidden" name="interestLanguage" id="selected-interests-lang" >
                  <button type="button" class="myinfo-interest-lang-btn" value="1">Java</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="2">Python</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="3">C#</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="4">C++</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="5">JavaScript</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="6">Ruby</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="7">Swift</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="8">TypeScript</button>
                  <button type="button"  class="myinfo-interest-lang-btn" value="9">PHP</button>
                </div>
              </div>

              <!--  01-06-02 프레임워크  -->
              <div class="myinfo-form-select-interest">
                <span class="myinfo-form-select-interest-title">Framework</span>
                <div class="myinfo-form-select-interest-button">
                  <input type="hidden" name="interestFramework" id="selected-interests-fw" >
                  <button type="button" class="myinfo-interest-fw-btn" value="1">Spring</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="2">React</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="3">Angular</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="4">Vue.js</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="5">Express.js</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="6">Django</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="7">Ruby on Rails</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="8">Flask</button>
                  <button type="button"  class="myinfo-interest-fw-btn" value="9">Laravel</button>
                </div>
              </div>

              <!--  01-06-03 직무  -->
              <div class="myinfo-form-select-interest">
                <span class="myinfo-form-select-interest-title">Role</span>
                <div class="myinfo-form-select-interest-button">
                  <input type="hidden" name="interestJob" id="selected-interests-job">
                  <button type="button" class="myinfo-interest-job-btn" value="1">프론트엔드</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="2">백엔드</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="3">풀스택</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="4">모바일 앱 개발</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="5">게임 개발</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="6">데이터베이스</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="7">데브옵스</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="8">디자이너</button>
                  <button type="button"  class="myinfo-interest-job-btn" value="9">기획자</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="frame-bottom">
          <div class="myinfo-updatebtn" @click="submitupdateInfo()">수정</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
  import axios from "axios";
  import {onMounted, reactive, ref, watch} from "vue";
  import {useRoute} from "vue-router";
  import {api, apiToken} from "@/common.js";
  const token = localStorage.getItem("jwtToken");
  const route = useRoute();//CompositionAPI 매칭된 라우트 (OptionAPI : this.$route)
  const getDataErr = reactive({});
  const myInfo = ref({
    "userId" : "",
    "password" : "",
    "email" : "",
    "nickname" : "",
    "location1" : "",
    "location2" : "",
    "interestLanguage" : "",
    "interestFramework" : "",
    "interestJob" : ""
  });

  // message
  const msg = ref({
    password: '',
    email: '',
    nickname: '',
    location1: ''
  })

  // 유효성 검사 조건
  const passwordRegex = /^[a-zA-Z0-9]{6,}$/;
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,20}$/;

  // 검사 여부 확인
  const check = reactive({
    password: false,
    email: false,
    nickname: false,
    location1: false,
  });

  // 유효성 검사 & 중복 확인
  const validatePassword = () => {
    if (!myInfo.value.password || !passwordRegex.test(myInfo.value.password)) {
      check.password = false;
      msg.value.password = "비밀번호는 최소 6자 이상, 알파벳과 숫자로 입력해주세요.";
    } else {
      check.password = true;
      msg.value.password = "";
    }
  };
  const validateEmail = () => {
    if (!myInfo.value.email || !emailRegex.test(myInfo.value.email)) {
      check.email = false;
      msg.value.email = "올바른 이메일 주소를 입력해주세요.";
    } else {
      check.email = true;
      msg.value.email = "";
    }
  };
  const validateNickname = async () => {
    const value = myInfo.value.nickname;
    if (!myInfo.value.nickname || !nicknameRegex.test(myInfo.value.nickname)) {
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

  watch(() => myInfo.password, validatePassword);
  watch(() => myInfo.email, validateEmail);
  watch(() => myInfo.nickname, validateNickname);

  //지역1 위치 인증
  const getLocation = async () => {
    // apiKey 가져오기
    const apiResponse = await axios.get(`http://43.201.149.206:8081/users/apiKey`);
    const apiKey = apiResponse.data;
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
          console.log(response.data.documents[0]);
          myInfo.value.location1 = response.data.documents[0].region_1depth_name;
        }
      });
    } else {
      alert('브라우저가 위치 정보를 지원하지 않습니다.');
    }
  };

  //지역2 위치 인증
  const getLocation2 = async () => {
    // apiKey 가져오기
    const apiResponse = await axios.get(`http://43.201.149.206:8081/users/apiKey`);
    const apiKey = apiResponse.data;
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
          console.log(response.data.documents[0]);
          myInfo.value.location2 = response.data.documents[0].region_1depth_name;
        }
      });
    } else {
      alert('브라우저가 위치 정보를 지원하지 않습니다.');
    }
  };
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
        switch (type) {
          case "lang":
            myInfo.value.interestLanguage = selectedInterestsInput.value
            break
          case "fw":
            myInfo.value.interestFramework = selectedInterestsInput.value
            break
          case "job":
            myInfo.value.interestJob = selectedInterestsInput.value
        }
        //선택된 버튼은 class가 myinfo-interest-lang-btn selected 됨
        console.log(myInfo.value.interestLanguage);
        console.log(myInfo.value.interestFramework);
        console.log(myInfo.value.interestJob);
      })
    })
  }
  let selectedInterestsLang = [];
  let selectedInterestsFw = [];
  let selectedInterestsJob = [];

  //수정
  async function submitupdateInfo() {
    //유효성 검사 & 중복 확인
    validatePassword();
    validateEmail();
    await validateNickname(); //중복검사라 await

    //if(myInfo.value.password === ""){
    if(check.password && check.email && check.nickname){
      if (!confirm("정말 수정하시겠습니까?")) {
        alert("취소되었습니다.");
        window.location.reload();
      } else {
        apiToken(
            "users/setting",
            "PUT",
            //myInfo.value
            {
              password: myInfo.value.password,
              email: myInfo.value.email,
              nickname: myInfo.value.nickname,
              location1: myInfo.value.location1,
              location2: myInfo.value.location2,
              interestLanguage: myInfo.value.interestLanguage,
              interestFramework: myInfo.value.interestFramework,
              interestJob: myInfo.value.interestJob
            },
            token)
            .then(response => {
              if (response instanceof Error) {
                console.log(response);
              } else {
                console.log(response);
                alert("수정완료되었습니다.");
                window.location.reload();
              }
            });
          }
    }else{
      alert("다시 확인해주세요");
    }
  }

  onMounted(()=>{
    apiToken("users/setting", "GET", null, token)
        .then( async (response) => {
          if(response instanceof Error){
            console.log(response);
          }else {
            myInfo.value = await response;
            myInfo.value.password="";
            console.log(myInfo.value);
          }
          return response;
        }).then(async (response)=>{
            let langarr = (response.interestLanguage) ? response.interestLanguage.split("_") : [];
            let langs = langarr.filter((element) => element !== "");
            console.log(langarr);
            //프레임워크
            let fwarr = (response.interestFramework) ? response.interestFramework.split("_") : []
            let fws = fwarr.filter((element) => element !== "");
            console.log(fwarr);
            //직무
            let jobarr = (response.interestJob) ? response.interestJob.split("_") : [];
            let jobs = jobarr.filter((element) => element !== "");
            console.log(jobarr);
            //관심언어
            let langButtons = document.querySelectorAll('.myinfo-interest-lang-btn');
            for (let lang of langs) {
              for (let langBtn of langButtons) {
                if (lang === langBtn.value) {
                  langBtn.className = "myinfo-interest-lang-btn selected";
                  selectedInterestsLang.push(langBtn.value);
                }
              }
            }
            //관심 프레임워크
            let fwButtons = document.querySelectorAll('.myinfo-interest-fw-btn');
            for (let fw of fws) {
              for (let fwBtn of fwButtons) {
                if (fw === fwBtn.value) {
                  fwBtn.className = "myinfo-interest-fw-btn selected";
                  selectedInterestsFw.push(fwBtn.value);
                }
              }
            }
            //관심 직무
            let jobButtons = document.querySelectorAll('.myinfo-interest-job-btn');
            for (let job of jobs) {
              for (let jobBtn of jobButtons) {
                if (job === jobBtn.value) {
                  jobBtn.className = "myinfo-interest-job-btn selected";
                  selectedInterestsJob.push(jobBtn.value);
                }
              }
            }
        })
        // 언어
        const interestLangButtons = document.querySelectorAll('.myinfo-interest-lang-btn');
        const selectedInterestsLangInput = document.getElementById('selected-interests-lang');
        selectInterests(interestLangButtons, selectedInterestsLang, selectedInterestsLangInput, "lang");
        // 프레임 워크
        const interestFwButtons = document.querySelectorAll('.myinfo-interest-fw-btn');
        const selectedInterestsFwInput = document.getElementById('selected-interests-fw');
        selectInterests(interestFwButtons, selectedInterestsFw, selectedInterestsFwInput, "fw");
        // 직무
        const interestJobButtons = document.querySelectorAll('.myinfo-interest-job-btn');
        const selectedInterestsJobInput = document.getElementById('selected-interests-job');
        selectInterests(interestJobButtons, selectedInterestsJob, selectedInterestsJobInput, "job");
  });
</script>

<style src="../../assets/css/myinfo.css" scoped>

</style>