<script setup>
import {defineProps, onMounted, ref} from "vue";
import {api, apiToken} from "@/common.js";
import {useRoute, useRouter} from "vue-router";
import { useAuthStore } from '@/stores/index';
const router = useRouter()
const route = useRoute()
let p = defineProps({
  type: String
});
if (!localStorage.getItem("jwtToken")){
  alert("로그인을 해 주세요!")
  router.replace("/login")
}
function write() {
  if (!writeVal.value.title){
    alert("제목을 입력해 주세요")
    return;
  }
  if (!writeVal.value.applicationDeadline){
    alert("모집기간을 적어주세요")
    return;
  }
  if (!writeVal.value.recruitmentCount){
    alert("모집 인원을 입력을 확인 해 주세요")
    return;
  }
  if (!writeVal.value.description){
    alert("내용을 확인 해 주세요")
    return;
  }
  //수정 모드
  if (p.type === "fix") {
    apiToken(
        "meeting",
        "PUT",
        writeVal.value,
        localStorage.getItem("jwtToken")
    ).then(
        () =>
        { alert("수정 되었습니다.")
          router.go(-1);
        }
    )
  } else {
    //쓰기 모드
    apiToken(
        "meeting",
        "POST",
        writeVal.value,
        localStorage.getItem("jwtToken")
    ).then(
        response => {
          router.replace("/meet")
        }
    )
  }
}




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
          writeVal.value.interestLanguage = selectedInterestsInput.value
          break
        case "fw":
          writeVal.value.interestFramework = selectedInterestsInput.value
          break
        case "job":
          writeVal.value.interestJob = selectedInterestsInput.value
      }
      //선택된 버튼은 class가 signup-interest-lang-btn selected 됨

    })
  })
}
let selectedInterestsLang = []
let selectedInterestsFw = []
let selectedInterestsJob = []

function countText() {
  contentCount.value = writeVal.value.description.length
}


// 글자수 세기
const contentCount = ref(0)
const writeVal = ref({
  "title": "",
  "category": 1,
  "applicationDeadline": null,
  "recruitmentCount": 0,
  "location": "서울특별시",
  "description": "",
  "interestLanguage": "",
  "interestFramework": "",
  "interestJob": "",
});




onMounted(() => {
  if (p.type === "fix") {
    // => 원래 데이터 받아 오기
    api(
        "meeting/" + route.params.fix_id,
        "GET",
        ""
    ).then(
        async (response) => {
          // console.log(response)
          writeVal.value.description = await response.description
          writeVal.value.location = response.location
          writeVal.value.title = response.title
          writeVal.value.applicationDeadline = response.applicationDeadline
          writeVal.value.meetingId = route.params.fix_id
          writeVal.value.status = response.status
          writeVal.value.recruitmentCount = response.recruitmentCount
          writeVal.value.interestLanguage = response.interestLanguage
          writeVal.value.interestFramework = response.interestFramework
          writeVal.value.interestJob = response.interestJob
          document.querySelector("#radio" + response.category).setAttribute("checked", "checked")
          countText()
          return response
        }
    ).then(
        (response) =>{
          // 버튼 누르기
          let langarr = (response.interestLanguage) ? response.interestLanguage.split("_") : [];
          let langs = langarr.filter((element) => element !== "");
          //프레임워크
          let fwarr = (response.interestFramework) ? response.interestFramework.split("_") : []
          let fws = fwarr.filter((element) => element !== "");
          //직무
          let jobarr = (response.interestJob) ? response.interestJob.split("_") : [];
          let jobs = jobarr.filter((element) => element !== "");
          // console.log(response.interestJob)
          let langButtons = document.querySelectorAll('.signup-interest-lang-btn');
          for (let lang of langs) {
            for (let langBtn of langButtons) {
              if (lang === langBtn.value) {
                langBtn.className = "signup-interest-lang-btn selected";
                selectedInterestsLang.push(langBtn.value);
              }
            }
          }
          //관심 프레임워크
          let fwButtons = document.querySelectorAll('.signup-interest-fw-btn');
          for (let fw of fws) {
            for (let fwBtn of fwButtons) {
              if (fw === fwBtn.value) {
                fwBtn.className = "signup-interest-fw-btn selected";
                selectedInterestsFw.push(fwBtn.value);
              }
            }
          }
          //관심 직무
          let jobButtons = document.querySelectorAll('.signup-interest-job-btn');
          for (let job of jobs) {
            for (let jobBtn of jobButtons) {
              if (job === jobBtn.value) {
                jobBtn.className = "signup-interest-job-btn selected";
                selectedInterestsJob.push(jobBtn.value);
              }
            }
          }
        }

    )
  }



// 언어
  const interestLangButtons = document.querySelectorAll('.signup-interest-lang-btn');
  const selectedInterestsLangInput = document.getElementById('selected-interests-lang');
  selectInterests(interestLangButtons, selectedInterestsLang, selectedInterestsLangInput, "lang");
// 프레임 워크
  const interestFwButtons = document.querySelectorAll('.signup-interest-fw-btn');
  const selectedInterestsFwInput = document.getElementById('selected-interests-fw');
  selectInterests(interestFwButtons, selectedInterestsFw, selectedInterestsFwInput, "fw");
// 직무
  const interestJobButtons = document.querySelectorAll('.signup-interest-job-btn');
  const selectedInterestsJobInput = document.getElementById('selected-interests-job');
  selectInterests(interestJobButtons, selectedInterestsJob, selectedInterestsJobInput, "job");





  //관심 언어

  // 관심 분야 선택
  // 버튼 목록, 선택된 관심사 배열, hidden input에 저장


})
</script>

<template>
  <article>
    <h2><input class="title-input" name="title" placeholder="제목을 입력해 주세요" maxlength=50 minlength="2"
               v-model="writeVal.title" required ></h2>
    <section>
      <div id="content_type">
        <table>
          <tr>
            <th>카테고리</th>
            <td>
              <input id="radio1" type="radio" name="category" value=1 @click="()=> writeVal.category = 1" checked> 프로젝트
              <input id="radio0" type="radio" name="category" value=0 @click="()=> writeVal.category = 0" class="category-radio"> 스터디
              <input id="radio2" type="radio" name="category" value=2 @click="()=> writeVal.category = 2" class="category-radio"> 기타
            </td>
          </tr>
          <tr>
            <th>마감일</th>
            <td>
              <label>
                <input id="endDate" name="endDate" type="date" v-model="writeVal.applicationDeadline" required/>
              </label>
            </td>
          </tr>
          <tr>
            <th>모집 장소</th>
            <td>
              <div>
                <span v-text="writeVal.location"></span>
                <input type="hidden" name="location" v-model="writeVal.location">
              </div>
            </td>
          </tr>
          <tr>
            <th>모집 인원 수</th>
            <td>
              <input id="recruitNum" name="recruitNum" type="number" placeholder="(최대 100 명)" min="0" max="100"
                     v-model="writeVal.recruitmentCount" required>
            </td>
          </tr>
          <tr>
            <th>관심 분야</th>
            <td style="padding: 0 10px 10px 0;">
              <div class="signup-form-input-box-content" style="padding-bottom: 10px;">
                <!--  02-02-06-01 언어  -->
                <div class="signup-form-select-interest">
                  <span class="signup-form-select-interest-title">Language</span>
                  <div class="signup-form-select-interest-button">
                    <input type="hidden" name="interest_language" id="selected-interests-lang" value="">
                    <button type="button" class="signup-interest-lang-btn" value="1">Java</button>
                    <button type="button" class="signup-interest-lang-btn" value="2">Python</button>
                    <button type="button" class="signup-interest-lang-btn" value="3">C#</button>
                    <button type="button" class="signup-interest-lang-btn" value="4">C++</button>
                    <button type="button" class="signup-interest-lang-btn" value="5">JavaScript</button>
                    <button type="button" class="signup-interest-lang-btn" value="6">Ruby</button>
                    <button type="button" class="signup-interest-lang-btn" value="7">Swift</button>
                    <button type="button" class="signup-interest-lang-btn" value="8">TypeScript</button>
                    <button type="button" class="signup-interest-lang-btn" value="9">PHP</button>
                  </div>
                </div>

                <!--  02-02-06-02 프레임워크  -->
                <div class="signup-form-select-interest">
                  <span class="signup-form-select-interest-title">Framework</span>
                  <div class="signup-form-select-interest-button">
                    <input type="hidden" name="interest_framework" id="selected-interests-fw" value="">
                    <button type="button" class="signup-interest-fw-btn" value="1">Spring</button>
                    <button type="button" class="signup-interest-fw-btn" value="2">React</button>
                    <button type="button" class="signup-interest-fw-btn" value="3">Angular</button>
                    <button type="button" class="signup-interest-fw-btn" value="4">Vue.js</button>
                    <button type="button" class="signup-interest-fw-btn" value="5">Express.js</button>
                    <button type="button" class="signup-interest-fw-btn" value="6">Django</button>
                    <button type="button" class="signup-interest-fw-btn" value="7">Ruby on Rails</button>
                    <button type="button" class="signup-interest-fw-btn" value="8">Flask</button>
                    <button type="button" class="signup-interest-fw-btn" value="9">Laravel</button>
                  </div>
                </div>

                <!--  02-02-06-01 직무  -->
                <div class="signup-form-select-interest">
                  <span class="signup-form-select-interest-title">Role</span>
                  <div class="signup-form-select-interest-button">
                    <input type="hidden" name="interest_job" id="selected-interests-job" value="">
                    <button type="button" class="signup-interest-job-btn" value="1">프론트엔드</button>
                    <button type="button" class="signup-interest-job-btn" value="2">백엔드</button>
                    <button type="button" class="signup-interest-job-btn" value="3">풀스택</button>
                    <button type="button" class="signup-interest-job-btn" value="4">모바일 앱 개발</button>
                    <button type="button" class="signup-interest-job-btn" value="5">게임 개발</button>
                    <button type="button" class="signup-interest-job-btn" value="6">데이터베이스</button>
                    <button type="button" class="signup-interest-job-btn" value="7">데브옵스</button>
                    <button type="button" class="signup-interest-job-btn" value="8">디자이너</button>
                    <button type="button" class="signup-interest-job-btn" value="9">기획자</button>
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </table>
      </div>
    </section>
    <section>
      <div id="content">
        <table>
          <tr>
            <th>
              <div>소개</div>
              <div id="text-length-check">글자수 :
                <span id="text-length-check-count" v-text="contentCount"></span>
              </div>
            </th>
            <td>
              <textarea id="description" name="content" placeholder="내용을 입력해 주세요!" required @keyup="countText()"
                        v-model="writeVal.description"></textarea>
            </td>
          </tr>
        </table>
      </div>
    </section>
    <div class="submit-btn-container">
      <button id="submit-button" type="button" @click="write()" class="submit-btn">등록하기</button>
    </div>
  </article>

</template>


<style scoped>
section{
  width: 100%;
}
.title-input{
  width: 100%;
  padding: 10px;
  padding-left: 15px;
}
#text-length-check{
  font-size: 16px;
  margin-top: 5px;
  color: grey;
}
#text-length-check-count{
  color: coral;
}
.category-radio{
  margin-left: 15px;
}
.submit-btn-container{
  text-align: right;
}
.submit-btn{
  background-color: #FF9F29; color: white; margin-top: 40px; border: 1px solid white;
  width: 100px;
  height: 40px;
  border-radius: 10px;
}
</style>

<style src="../../assets/css/meeting/meeting_home.css" scoped>
</style>
<style src="../../assets/css/meeting/meeting_article.css" scoped>
</style>
<style src="../../assets/css/home.css" scoped>
</style>
<style src="../../assets/css/meeting/meeting_form.css" scoped>
</style>
<style src="../../assets/css/signup.css"></style>