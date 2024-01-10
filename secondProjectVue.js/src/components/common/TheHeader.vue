<script setup>
import {onMounted, ref, watch} from "vue";
import { useAuthStore } from '@/stores/index';
import router from "@/router/index.js";

const auth = useAuthStore();

// 토큰, 검색어
const header = ref({
  jwtToken: auth.getToken(),
  searchWord: '',
});
// 로그아웃  시  Pinia 스토어에서 토큰 제거
const logout = async () => {
  auth.clearToken();
  await router.push('/');
  location.reload();
}

// 검색
const searchResult = () => {
  router.replace({ path: '/searching', query: { searchWord: header.value.searchWord } });
}

</script>

<template>
  <header>
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
            <router-link class="main-header-nav-meetinglist-text" :to="{ name: 'meeting', query: {category: 0}}"><span>스터디</span></router-link>
            <!-- 02-03-02 프로젝트 -->
            <router-link class="main-header-nav-meetinglist-text" :to="{ name: 'meeting', query: {category: 1}}"><span>프로젝트</span></router-link>
            <!-- 02-03-03 기타 -->
            <router-link class="main-header-nav-meetinglist-text" :to="{ name: 'meeting', query: {category: 2}}"><span>기타</span></router-link></div>

          <!-- 02-04 마이페이지(로그인한 경우) -->
          <router-link class="main-header-nav-mypage main-header-nav-meetinglist-text" to="/users/myprofile" v-if="header.jwtToken">
            <span>마이페이지</span>
          </router-link>
        </div>
        <div class="main-header-searchAndBtn">
          <!-- 03 검색 -->
          <form id="searchForm" action="/searching" method="get">
            <div class="main-header-search">
              <input v-model="header.searchWord" @keyup.enter="searchResult" class="main-header-search-input" name="searchWord" placeholder="검색해보세요!" type="text"/>
              <button class="main-header-search-button" type="submit" @click="searchResult">
                <div class="main-header-search-glass">
                  <img src="../../assets/image/home/search.png" style="width: 20px; height: 20px">

                </div>
              </button>
            </div>
          </form><!-- 04 로그인, 회원가입, 로그아웃 -->
          <div class="main-header-button">
            <!-- 로그인하지 않았을 경우 -->
            <router-link to="/login" v-if="!header.jwtToken">
              <button class="main-header-button-login">
                <span class="main-header-button-login-text"><span>로그인</span></span>
              </button>
            </router-link>
            <router-link to="/signup" v-if="!header.jwtToken">
              <button class="main-header-button-signup">
                <span class="main-header-button-signup-text"><span>회원가입</span></span>
              </button>
            </router-link>
            <button class="main-header-button-logout" @click="logout" v-if="header.jwtToken">
              <span class="main-header-button-logout-text"><span>로그아웃</span></span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
<style scoped src="@/assets/css/home.css"></style>