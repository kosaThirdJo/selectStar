import {createRouter, createWebHistory} from "vue-router";
// common
import home from "@/pages/Home.vue";
import login from "@/pages/user/login.vue";
import signup from "@/pages/user/signup.vue";
import search from "@/pages/search.vue";
// meeting
import meeting from "@/pages/meeting/Meeting.vue";
import meetingDetail from "@/pages/meeting/meetingDetail.vue";
import meetingFrom from "@/pages/meeting/meetingForm.vue"
import meetingFix from "@/pages/meeting/MeetingFix.vue";
// mypage
import mypage from "../pages/Mypage.vue";
import myprofile from "../components/mypages/myprofile.vue";
import myinfo from "../components/mypages/myinfo.vue"
import mymeetingList from "../components/mypages/mymeetingList.vue"
import myapplyingList from "../components/mypages/myapplyingList.vue"
//user profile
import userprofile from "../pages/userprofile.vue"

// 동적 매핑법 :변수
const router = createRouter({
    history: createWebHistory(),
    routes:
        [
            // common
            {path: '/', component: home},
            {path:'/login', component: login},
            {path:'/signup', component: signup},
            {path:'/searching', component: search},
            // meeting
            {path: '/meet', name: 'meeting', component: meeting},
            {path: '/meet/:post_id', component: meetingDetail},
            {path: '/meet/write',component:meetingFrom},
            {path: '/meet/:post_id', name: 'meetingDetail', component: meetingDetail},
            {path: '/meet/write', name: 'meetingForm', component:meetingFrom},
            {path: '/meet/fix/:fix_id', name: 'meetingFix', component:meetingFix},
            // mypage
            //{path: '/users/:user_id', name: 'mypage', component:mypage,
            {path: '/users', name: 'mypage', component:mypage,
                children:[
                    {path:'myprofile', name:'myprofile', component: myprofile},
                    {path:'myinfo', name:'myinfo', component: myinfo},
                    {path: 'mymeeting', name: 'mymeetinglist',component:mymeetingList},
                    {path: 'myapplying', name: 'myapplyinglist',component:myapplyingList}
                ]},
            //user profile
            {path: '/profiles/info/:user_id', name: 'userprofile', component: userprofile}

        ]
})
export default router;