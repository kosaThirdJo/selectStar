import { defineStore } from 'pinia';
import { ref } from "vue";

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    const role = ref('');
    function setToken(token){
        this.token = token;
        localStorage.setItem('jwtToken', token);
    }
    function setRole(role) { // 추가: 사용자의 역할 정보를 설정하는 메서드
        this.role = role;
    }
    function clearToken() {
        this.token = null;
        this.role = '';
        localStorage.removeItem('jwtToken');
    }
    function getToken(){
        return localStorage.getItem('jwtToken');
    }

    return {token, setToken, clearToken, getToken, role, setRole}

})
