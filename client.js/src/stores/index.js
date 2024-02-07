import { defineStore } from 'pinia';
import { ref } from "vue";

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    const role = ref('');

    const login = (newToken, newRole) => {
        token.value = newToken;
        role.value = newRole;
        localStorage.setItem('jwtToken', newToken);
    };
    const logout = () => {
        token.value = '';
        role.value = '';
        localStorage.removeItem('jwtToken');
    };
    const getToken = () => {
        // return token.value;
        return localStorage.getItem('jwtToken');
    };

    const getRole = () => {
        return role.value;
    };
    /*function setToken(token){
        this.token = token;
        localStorage.setItem('jwtToken', token);
    }
    function clearToken() {
        this.token = null;
        this.role = '';
        localStorage.removeItem('jwtToken');
    }*/
/*    function getToken(){
        return localStorage.getItem('jwtToken');
    }*/
/*    function setRole(role) {
        this.role = role;
    }*/
    return {login, logout, getToken, getRole, token, role}

})
