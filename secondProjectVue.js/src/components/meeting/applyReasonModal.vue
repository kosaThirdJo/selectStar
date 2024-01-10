<script setup>
import {api, apiToken} from "@/common.js";
import router from "../../router/index.js";
import {ref} from "vue";

const props = defineProps({
  show: Boolean,
  userId: Number,
  meetingId: Number
})
let validObj = ref([]);
let rejectObj = ref([])


function rejectApply(idx){
  const result = confirm("거절 하실껀가요?");
  if(result) {
  } else {
    alert("거절 취소되었습니다.");
    return;
  }

  apiToken("apply/reject",
      "PATCH",
      {
        meetingId:props.meetingId,
        userId:validObj.value[idx].userId,
        reason:rejectObj.value[idx]
      },
      localStorage.getItem("jwtToken")
  ).then(
      (response) =>{
      alert("거절하셨습니다.")
        router.go(0);
  })

}

function GetValidApply(){
  apiToken("apply/meeting/valid?meetingId="+props.meetingId,
  "GET",
      "",
      localStorage.getItem("jwtToken")
      ).then(
          response =>{
            validObj.value = response;

          }
          )
}
GetValidApply()

</script>

<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask">
      <div class="modal-container">
        <div class="modal-header">
          <slot name="header"></slot>
        </div>
        <div class="modal-body">
          <slot v-if="validObj.length===0" name="body">
            <h4 style="color: red"> 신청자가 없어요</h4>
          </slot>
          <slot v-for="(applyEle,idx) in validObj" name="body">
            <div v-text="'유저 :' + applyEle.userName"></div>
            <div v-text="'이메일 주소 :' + applyEle.emailAddress"></div>
            <div v-text="'블로그 및 깃허브 :' + applyEle.snsAddress"></div>
            <div v-text="'신청 사유 :' +applyEle.reason"></div>
            <input v-model="rejectObj[idx]" placeholder="거절 사유를 입력 해 주세요"> <button @click="rejectApply(idx)">거절</button>
<!--            click 할 경우 ajax-->
            <hr>
          </slot>
        </div>
        <div class="modal-footer">
          <slot name="footer">
            <button
                class="modal-default-button"
                @click="$emit('close')"
            >닫기</button>
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