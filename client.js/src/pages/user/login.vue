<script setup>
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from "axios";
import { useAuthStore } from '@/stores/index';
import {loginApi} from "@/common.js";
const auth = useAuthStore();
const router = useRouter();
const loginInfo = ref({
  name: '',
  password: '',
})

axios.defaults.withCredentials = true;
const loginHandler = async () => {
  try {
    const response = await loginApi('login', 'POST', loginInfo.value);

    if (response.status === 200) {
      const token = response.headers['authorization'];
      const role = response.headers['role'];
      auth.login(token, role);
      if (role === 'USER') {
        location.replace('/');
      } else if (role === 'ADMIN') {
        location.replace('/admin/users');
      }
    } else if (response.status === 403) {
      if (response.data && response.data.message) {
        alert(response.data.message);
      }
    } else {
      console.error('로그인 실패:', response.status, response.data);
      alert('아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.');
    }
  } catch (error) {
    console.error('로그인 오류:', error);
    alert('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
  }
};

</script>


<template>
  <div class="login-container">
    <div class="login-content">
      <div class="login-top">
        <router-link to="/" class="login-top-logo">
          <span class="login-top-logo-text">
              SELECT <span class="login-top-logo-text-star">*</span>
          </span>
        </router-link>
        <span class="login-top-intro1">
          <span>SELECT*에 오신것을 환영합니다.</span>
        </span>
        <span class="login-top-intro2">
          <span>SELECT*는 개발자를 위한 모임 커뮤니티입니다.</span>
        </span>
      </div>

      <div class="login-form">
        <!-- 로그인 form  -->
        <form @submit.prevent="loginHandler ">
          <div class="login-form-input">
            <!--  아이디  -->
            <div class="login-form-input-box">
              <span class="login-form-input-box-title">아이디</span>
              <div class="login-form-input-withbtn">
                <input v-model="loginInfo.name" type="text" id="name" name="loginName"
                       class="login-form-input-box-content-withbtn"/>
              </div>
              <div class="login-form-input-check-alert" id="checkId">
                <span></span>
              </div>
            </div>

            <!--  비밀번호  -->
            <div class="login-form-input-box">
              <span class="login-form-input-box-title">비밀번호</span>
              <input v-model="loginInfo.password" type="password" id="password" name="loginPassword"
                     class="login-form-input-box-content"/>
              <div class="login-form-input-check-alert" id="checkPassword">
                <span></span>
              </div>
            </div>
            <!-- 로그인 버튼 -->
            <button type="submit" class="login-form-submit">
              <span class="login-form-submit-text"><span>로그인</span></span>
            </button>
          </div>
        </form>

        <!-- 회원가입 페이지 이동-->
        <div class="login-movetoSignup">
          <span>아직 회원이 아니신가요?</span>
          <router-link to="/signup" class="login-movetoLogin-SignupPg">회원가입</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped src="../../assets/css/login.css">

</style>