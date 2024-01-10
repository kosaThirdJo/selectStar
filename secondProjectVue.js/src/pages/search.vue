<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";
import { api } from "../common.js";
import Card from "@/components/element/card.vue";
import SearchNicknameCard from "@/components/element/searchNicknameCard.vue";

const route = useRoute()
const searchWord = route.query.searchWord;
const isLoading = ref(true);

// 검색어 검색 결과 요청
const searchMeetingResult = ref([]);
const searchNicknameResult = ref([]);
const showCount = ref(6);  // 더보기 (기본 6개 출력)
const showMemberCount = ref(8); // 회원 (기본 8개 출력)

api(
    `search?searchWord=${searchWord}`,
    "GET", ""
).then(response => {
  isLoading.value = false;
  searchMeetingResult.value = response[0];
  searchNicknameResult.value = response[1];
}).catch(error => {
  console.error(error);
});

// 검색 필터 버튼 활성화
const showBtn = ref(false);
function filterButton() {
  showBtn.value = !showBtn.value;
}

// 필터 검색 결과 요청
const category = [
  { label: "스터디", value: 0 },
  { label: "프로젝트", value: 1 },
  { label: "기타", value: 2 }
];
const languages = [
  { label: "Java", value: 1 },
  { label: "Python", value: 2 },
  { label: "C#", value: 3 },
  { label: "C++", value: 4},
  { label: "JavaScript", value: 5},
  { label: "Ruby", value: 6},
  { label: "Swift", value: 7},
  { label: "TypeScript", value: 8},
  { label: "PHP", value: 9},
];
const frameworks = [
  { label: "Spring", value: 1 },
  { label: "React", value: 2 },
  { label: "Angular", value: 3 },
  { label: "Vue.js", value: 4},
  { label: "Express.js", value: 5},
  { label: "Django", value: 6},
  { label: "Ruby on Rails", value: 7},
  { label: "Flask", value: 8},
  { label: "Laravel", value: 9},
];
const jobs = [
  { label: "프론트엔드", value: 1 },
  { label: "백엔드", value: 2 },
  { label: "풀스택", value: 3 },
  { label: "모바일 앱 개발", value: 4},
  { label: "게임 개발", value: 5},
  { label: "데이터베이스", value: 6},
  { label: "데브옵스", value: 7},
  { label: "디자이너", value: 8},
  { label: "기획자", value: 9},
];
const selectedFilters = ref({
  category: [],
  languages: [],
  frameworks: [],
  jobs: [],
})
// 필터 검색 결과
const filterSearchResult = () =>{
  showCount.value = 6;
  const categoryUrn = selectedFilters.value.category.length > 0 ? `&category=${selectedFilters.value.category}` : '';
  const languageUrn = selectedFilters.value.languages.length > 0 ? `&languages=${selectedFilters.value.languages}` : '';
  const frameworkUrn = selectedFilters.value.frameworks.length > 0 ? `&frameworks=${selectedFilters.value.frameworks}` : '';
  const jobUrn = selectedFilters.value.jobs.length > 0 ? `&jobs=${selectedFilters.value.jobs}` : '';
  const urn = `search/filter?searchWord=${searchWord}${categoryUrn}${languageUrn}${frameworkUrn}${jobUrn}`;
  api(urn,"GET", "")
    .then(response => {
      console.log(response);
      searchMeetingResult.value = response;
  }).catch(error => {
    console.error(error);
  });
}

const filterButtonClick = (filterType, value, event) => {
  const button = event.target;
  // 필터 버튼 선택
  if (!selectedFilters.value[filterType].includes(value)) {
    button.classList.add("selected");  // class 속성에 seleted 추가
    selectedFilters.value[filterType].push(value);  // 배열에 추가
  } else {
    button.classList.remove("selected");
    selectedFilters.value[filterType] = selectedFilters.value[filterType].filter((element) => element !== value);
  }
  filterSearchResult();  // 검색 결과 갱신
};

// 검색 초기화 버튼
const resetFilter = () => {
  const filterButtons = document.querySelectorAll(".search-filter-select-btn");
  filterButtons.forEach(button => {
    button.classList.remove("selected");   // 검색 필터 버튼의 class 속성 제거
  });
  // selectedFilters.value 초기화
  selectedFilters.value = {
    category: [],
    languages: [],
    frameworks: [],
    jobs: []
  };
  filterSearchResult();
};
// 더보기 버튼 클릭 이벤트
const showMore = () => {
  showCount.value += 6;
};const showMemberMore = () => {
  showMemberCount.value += 8;
};
</script>

<template>
  <div class="search-container">
    <div v-if="!isLoading" class="search-result">
      <div class="search-title">
        <span class="search-title-word">{{ searchWord }}</span>
        <span v-if="searchWord" class="search-title-result">검색결과</span>
        <span v-if="!searchWord" class="search-title-result">전체 검색결과</span>
      </div>
      <!-- 모임 결과 시작 -->
      <div class="search-meeting-result">
        <!-- search filter -->
        <div class="search-filter">
          <!-- search filter top -->
          <div class="search-filter-top">
            <div class="search-filter-text-meeting">모임</div>
<!--검색 필터 버튼-->
            <button class="search-filter-btn" v-on:click="filterButton">
              <div class="search-filter-btn-title">
                <div class="search-filter-btn-title-icon"><span class="material-icons">tune</span></div>
                <span class="search-filter-btn-title-text"><span>검색 필터</span></span>
              </div>
            </button>
          </div>
          <!-- search filter select -->
          <div v-show="showBtn" class="search-filter-select">
            <!-- search filter select meeting category -->
            <div class="search-filter-select-list-meeting">
              <span class="search-filter-select-title">모임</span>
              <div class="search-filter-select-category">
                <button v-for="cate in category" :key="cate.value"
                    @click="($event) => filterButtonClick('category', cate.value, $event)"
                    class="search-filter-select-btn" type="button" :value="cate.value"
                >{{ cate.label }}
                </button>
              </div>
            </div>
            <!-- search filter select language category -->
            <div class="search-filter-select-list-language">
              <span class="search-filter-select-title">언어</span>
              <div class="search-filter-select-category">
                <button v-for="lang in languages" :key="lang.value"
                    @click="($event) => filterButtonClick('languages', lang.value, $event)"
                    class="search-filter-select-btn" type="button" :value="lang.value"
                >{{ lang.label }}
                </button>
              </div>
            </div>
            <!-- search filter select framework category -->
            <div class="search-filter-select-list-framework">
              <span class="search-filter-select-title">프레임워크</span>
              <div class="search-filter-select-category">
                <button v-for="fw in frameworks" :key="fw.value"
                        @click="($event) => filterButtonClick('frameworks', fw.value, $event)"
                        class="search-filter-select-btn" type="button" :value="fw.value"
                >{{ fw.label }}
                </button>
              </div>
            </div>
            <!-- search filter select job category -->
            <div class="search-filter-select-list-job">
              <span class="search-filter-select-title">직무</span>
              <div class="search-filter-select-category">
                <button v-for="job in jobs" :key="job.value"
                        @click="($event) => filterButtonClick('jobs', job.value, $event)"
                        class="search-filter-select-btn" type="button" :value="job.value"
                >{{ job.label }}
                </button>
              </div>
            </div>

            <div class="search-filter-select-reset">
              <button class="search-filter-select-reset-button" @click="resetFilter">
                <div class="search-filter-select-reset-button-icon"><span class="material-icons">replay</span></div>
                <span class="search-filter-select-reset-button-text"><span>초기화</span></span>
              </button>
            </div>
          </div>
        </div>
        <!-- 모임 검색 결과 -->
        <div class="search-filter-result">
          <!-- 모임 검색 결과 있을 경우 -->
          <card v-if="searchMeetingResult.length>0" v-for="(resOne, i) in searchMeetingResult.slice(0, showCount)" :key="i" :resOne="resOne" class="search-filter-result-container"></card>
          <!-- 모임 검색 결과 없을 경우 -->
          <div v-if="searchMeetingResult.length===0" class="search-filter-result-none">
            <div class="search-filter-result-none-result">
              <span v-if="searchWord"><span class="search-filter-result-none-searchWord">{{ searchWord }}</span>에 대한 모임 결과가 없습니다.</span>
              <span v-if="!searchWord">모임 결과가 없습니다.</span>
            </div>
            <div>찾으시는 모임이 없나요? 직접 만들어 보세요!</div>
            <div class="search-filter-result-none-btn">
              <a href="/meeting/write" class="search-filter-result-none-meetingbtn">모임 만들기</a>
            </div>
          </div>
        </div>
        <!-- 더보기 버튼 -->
        <div v-if="searchMeetingResult.length>showCount">
          <button class="search-morebtn" @click="showMore()">
            <div class="search-morebtn-icon"><span class="material-icons">expand_more</span></div>
            <span class="search-morebtn-text"><span>더보기</span></span>
          </button>
        </div>
      </div>
      <!-- 모임 검색 결과 끝 -->

      <!-- 사용자 검색 정보 -->
      <div class="search-nickname-result">
        <div class="search-nickname-title">회원</div>
        <div class="search-nickname-content">
          <!--회원 검색 결과 있을 경우-->
          <search-nickname-card v-if="searchNicknameResult.length>0" v-for="(resOne, i) in searchNicknameResult.slice(0, showMemberCount)" :key="i" :resOne="resOne" class="search-nickname-info"></search-nickname-card>
          <!--회원 검색 결과 없을 경우-->
          <div v-if="searchNicknameResult.length===0" class="search-nickname-noUser">찾으시는 회원이 존재하지 않습니다.</div>
        </div>
        <!-- 더보기 버튼 -->
        <div v-if="searchNicknameResult.length>showMemberCount">
          <button class="search-morebtn" @click="showMemberMore()">
            <div class="search-morebtn-icon"><span class="material-icons">expand_more</span></div>
            <span class="search-morebtn-text"><span>더보기</span></span>
          </button>
        </div>
      </div>
    </div>
<!--    검색 로딩 화면 -->
    <div v-else class="search-loading">
      <div class="search-loading-gif">
        <img src="@/assets/image/global/Spin-loading.gif" alt="loading">
      </div>
      <div class="search-loading-title">
        <span class="search-title-word">{{ searchWord }}</span>
        <span v-if="searchWord" class="search-title-result">검색중</span>
        <span v-if="!searchWord" class="search-title-result">전체 검색중</span>
      </div>
    </div>
  </div>
</template>

<style scoped src="@/assets/css/search.css"></style>