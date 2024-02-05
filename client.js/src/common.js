import {
    reactive,
    computed,
    toRefs
} from 'vue';

import axios from 'axios';



// urn 이거 참조 https://www.elancer.co.kr/blog/view?seq=74
const apiToken = async (urn, method, data, token) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    // URL : URL 인대 URL URL이 같으면 생략 가능
    return (await axios({
        url,
        method,
        data,
        headers: {
            Authorization: token
        }
    }).catch(e => {
        console.log(e);
        return { data: e}; //error 발생 시 e 반환
    })).data
}
const apiToken2 = async (urn, method, data, token) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    // URL : URL 인대 URL URL이 같으면 생략 가능
    return (await axios({
        url,
        method,
        data,
        headers: {
            Authorization: token
        }
    }).catch(e => {
        console.log(e);
        return { data: e}; //error 발생 시 e 반환
    }))
}
const api = async (urn, method, data) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    // URL : URL 인대 URL URL이 같으면 생략 가능
    return (await axios({
        url,
        method,
        data
    }).catch(e => {
        console.log(e);
        return { data: e}; //error 발생 시 e 반환
    })).data
}
const api2 = async (urn, method, data) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    // URL : URL 인대 URL URL이 같으면 생략 가능
    return (await axios({
        url,
        method,
        data
    }).catch(e => {
        console.log(e);
        return { data: e}; //error 발생 시 e 반환
    }))
}
const apiTokenMpt = async (urn, method, data, token) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    console.log(method);
    console.log(url);

     return (await axios({
         url,
         method: method,
         data,
         headers: {
            //'Content-Type': 'multipart/form-data',
            Authorization: token
        }
    }).catch(e => {
        console.log(e);
        return { data: e}; //error 발생 시 e 반환
    }))
}
const loginApi = async (urn, method, data) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    return (await axios({
        url, method, data, headers: {
            "X-Requested-With": "XMLHttpRequest"
        }
    }).catch(e => {
        console.log(url);
        console.log(e);
        return{data: e};
    }))
}
export {
    api, apiToken, loginApi, api2, apiToken2, apiTokenMpt
};