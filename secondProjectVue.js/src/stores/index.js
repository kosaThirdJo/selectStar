import { defineStore } from 'pinia';
import { ref } from "vue";

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    function setToken(token){
        this.token = token;
        localStorage.setItem('jwtToken', token);
    }
    function clearToken() {
        this.token = null;
        localStorage.removeItem('jwtToken');
    }
    function getToken(){
        return localStorage.getItem('jwtToken');
    }

    return {token, setToken, clearToken, getToken}

})
