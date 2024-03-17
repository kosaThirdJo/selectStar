<script setup>
import {useAuthStore} from '@/stores/index';
import {useCookies} from 'vue3-cookies';
import router from '@/router/index.js';
import {ref} from "vue";
import {apiToken2} from "@/common.js";
import {da} from "vuetify/locale";
// TODO 로그인 했을 경우 슬라이드 활성화,
// TODO 버거 아이콘  알림 아이콘으로 바꾸기,
// TODO 로그인 후, 버튼 클릭시 => 데이터를 가져옴,  데이터 바인딩 하기
// TODO 헤더 패딩 조절할 것.


const {logout, getToken, getRole} = useAuthStore();
const {cookies} = useCookies();

const logoutHandler = async () => {
  try {
    await backendLogout();
    logout();
    cookies.remove('refreshToken');
    await router.push('/');
    location.reload();
  } catch (error) {
    console.error("로그아웃 오류", error);
  }
};

const backendLogout = async () => {
  try {
    const response = await apiToken2("logout", "POST", null, getToken);
    console.log(response);
    if (response.status === 200) {
      console.log("로그아웃 성공");
    }
  } catch (error) {
    console.error("로그아웃 오류", error);
  }
};

const searchResult = () => {
  router.replace({path: '/searching', query: {searchWord: searchWord}});
};

const jwtToken = getToken();
const searchWord = ref('');
const role = getRole();

const drawer = ref(false);

const alertItems = ref([]);
function getNotification (){
  drawer.value = !drawer.value
  apiToken2("meeting/notification",
  "GET",
"",localStorage.getItem("jwtToken")).then(response => {

    alertItems.value = response.data.content;
  }).catch(error => {
    console.error(error)
  }
  )
}

</script>
<template>
  <header>
    <v-layout
    >
    <div class="main-header">
      <div class="main-header-wrap">
        <!-- 01 로고 -->
        <router-link class="main-header-logo" to="/">
          <div class="main-header-logo-text">
            <div>SELECT</div>
            <div class="main-header-logo-text-star">*</div>
          </div>
        </router-link>
        <!-- 02 nav -->
        <div class="main-header-nav">
          <!-- 02-01 FROM -->
          <div class="main-header-nav-from"><span class="main-header-nav-from-text"><span>FROM</span></span></div>
          <!-- 02-02 모임 -->
          <router-link class="main-header-nav-meeting" to="/meet">
            <span class="main-header-nav-meeting-text"><span>모임</span></span>
          </router-link>
          <!-- 02-03 모임 리스트 -->
          <div class="main-header-nav-meetinglist">
            <!-- 02-03-01 스터디 -->
            <router-link :class="{'active-link': $route.query.category === '0'}"
                         :to="{ name: 'meeting', query: {category: 0}}"
                         class="main-header-nav-meetinglist-text">
              <span>스터디</span>
            </router-link>
            <!-- 02-03-02 프로젝트 -->
            <router-link :class="{'active-link': $route.query.category === '1'}"
                         :to="{ name: 'meeting', query: {category: 1}}"
                         class="main-header-nav-meetinglist-text">
              <span>프로젝트</span>
            </router-link>
            <!-- 02-03-03 기타 -->
            <router-link :class="{'active-link': $route.query.category === '2'}"
                         :to="{ name: 'meeting', query: {category: 2}}" class="main-header-nav-meetinglist-text"><span>기타</span>
            </router-link>
          </div>

          <!-- 02-04 마이페이지 / 관리자 페이지 -->
          <router-link v-if="jwtToken && role==='USER'" class="main-header-nav-mypage main-header-nav-meetinglist-text"
                       to="/users/myprofile">
            <span>마이페이지</span>
          </router-link>
          <router-link v-if="jwtToken && role==='ADMIN'" class="main-header-nav-mypage main-header-nav-meetinglist-text"
                       to="/admin/users">
            <span>관리자페이지</span>
          </router-link>
        </div>
        <div class="main-header-searchAndBtn">
          <!-- 03 검색 -->
          <form id="searchForm" action="/searching" method="get">
            <div class="main-header-search">
              <input v-model="searchWord" class="main-header-search-input" name="searchWord"
                     placeholder="검색해보세요!" type="text" @keyup.enter="searchResult"/>
              <button class="main-header-search-button" type="submit" @click="searchResult">
                <div class="main-header-search-glass">
                  <img src="../../assets/image/home/search.png" style="width: 20px; height: 20px">
                </div>
              </button>
            </div>
          </form><!-- 04 로그인, 회원가입, 로그아웃 -->
          <div class="main-header-button">
            <!-- 로그인하지 않았을 경우 -->
            <router-link v-if="!jwtToken" to="/login">
              <button class="main-header-button-login"
                      :class="{ 'main-header-button-login-active': $route.path === '/login'}">
                <span class="main-header-button-login-text"><span>로그인</span></span>
              </button>
            </router-link>
            <router-link v-if="!jwtToken" to="/signup">
              <button class="main-header-button-signup"
                      :class="{ 'main-header-button-signup-active': $route.path === '/signup'}">
                <span class="main-header-button-signup-text"><span>회원가입</span></span>
              </button>
            </router-link>
            <button v-if="jwtToken" class="main-header-button-logout" @click="logoutHandler">
              <span class="main-header-button-logout-text"><span>로그아웃</span></span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <v-app-bar-nav-icon
        @click.stop="getNotification"
    ></v-app-bar-nav-icon>
      <v-navigation-drawer v-model="drawer" location="right"
                           permanent>
        <v-list :items="alertItems">
          <v-list-item
              v-for="item in alertItems"
              v-text="item.content"
              @click="router.push(item.url)"
            ></v-list-item>
        </v-list>
      </v-navigation-drawer>
    </v-layout>
  </header>
</template>

<style scoped src="@/assets/css/home.css"></style>
