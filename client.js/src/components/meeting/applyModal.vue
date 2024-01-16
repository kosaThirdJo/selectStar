<script setup>
import {api, apiToken} from "@/common.js";
import router from "../../router/index.js";



const props = defineProps({
  show: Boolean,
  userId: Number,
  meetingId: Number
})
let applyObj = {
  userId : props.userId,
  meetingId : props.meetingId,
  emailAddress : "",
  snsAddress : "",
  reason : "",
}

function submitApply (){
  if (!localStorage.getItem("jwtToken")){
    alert("로그인을 해 주세요")
    router.replace("/login")
    return
  }
  if (!applyObj.emailAddress){
    alert("이메일을 입력해 주세요!")
    return;
  }
  if (!applyObj.reason){
    alert("사유를 입력 해 주세요")
  }
  apiToken("apply",
  "POST",
      applyObj,
      localStorage.getItem("jwtToken")
  )
  alert("신청되었습니다.")
  router.go(0);
}


</script>

<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div class="modal-container">
        <div class="modal-header">
          <slot name="header"></slot>
        </div>
        <div class="modal-body">
          <slot name="body">
            <div>이메일</div>
            <input v-model="applyObj.emailAddress" class="modal-input" placeholder="abc@naver.com">
          </slot>
          <slot name="body">
            <div>블로그나 깃허브 주소</div>
            <input v-model="applyObj.snsAddress" class="modal-input" placeholder="https://github.com/torvalds">
          </slot>
          <slot name="body">
            <div>신청 사유</div>
            <input v-model="applyObj.reason" class="modal-input" placeholder="신청 사유를 입력 해 주세요!">
          </slot >
        </div>

        <div class="modal-footer">
          <slot name="footer">
            <button
                class="modal-default-button"
                @click="submitApply()"
            >yes</button>
            <button
                class="modal-default-button"
                @click="$emit('close')"
            >no</button>
          </slot>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style>
.modal-input{
  width: 100%;
}
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  transition: opacity 0.3s ease;
}

.modal-container {
  width: 500px;
  margin: auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
}

.modal-header h3 {
  margin-top: 0;
  color: #42b983;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  float: right;
}

/*
 * The following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the modal transition by editing
 * these styles.
 */

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>