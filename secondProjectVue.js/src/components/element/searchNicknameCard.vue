<style src="@/assets/css/search.css" scoped>
</style>
<template>
  <router-link :to="'/profiles/info/'+userId">
    <img v-if="photo" class="search-user-profile-photo" :src="photo" alt="Image"/>
    <img v-if="!photo || photo.isEmpty" class="search-user-profile-photo" :src="defaultImg" alt="Image"/>
    <div class="search-nickname-info-name">{{ nickname }}</div>
    <div class="search-nickname-info-about" style="min-height: 100px">{{ aboutMe }}</div>
  </router-link>
</template>

<script setup>
import {ref, toRef} from 'vue'
import { defineProps } from 'vue';
import defaultImg from '@/assets/image/global/userdefaultimg.png';

let p = defineProps( {
  resOne : Object,
});

let pp = toRef(p,"resOne");
const userId = ref(pp.value.userId);
const photo = ref(pp.value.profilePhoto);
const nickname = ref(pp.value.nickname);
const aboutMe = ref(pp.value.aboutMe);
</script>


