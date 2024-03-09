import { defineStore } from 'pinia';
import { ref } from "vue";

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    const role = ref('');

    const login = (newToken, newRole) => {
        token.value = newToken;
        role.value = newRole;
        localStorage.setItem('jwtToken', newToken);
        localStorage.setItem('role', newRole);
    };
    const logout = () => {
        token.value = '';
        role.value = '';
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('role');
    };
    const getToken = () => {
        // return token.value;
        return localStorage.getItem('jwtToken');
    };

    const getRole = () => {
        // return role.value;
        return localStorage.getItem('role');
    };
    return {login, logout, getToken, getRole, token, role}

})
