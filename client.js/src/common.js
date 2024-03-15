import {
    reactive,
    computed,
    toRefs
} from 'vue';

import axios from 'axios';
import {useCookies} from 'vue3-cookies';
import router from "@/router/index.js";

const {cookies} = useCookies();

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
    const url = "http://" + window.location.hostname + ":8081/" + urn
    const refreshToken = cookies.get('refreshToken');
    console.log("apiToken2 url", url);
    try {
        // AccessToken 유효성 확인
        const accessUrl = `http://${window.location.hostname}:8081/users/validate-access-token`;
        const accessResponse = await axios.post(accessUrl, null, {
            headers: {
                Authorization: token
            }
        });
        if (accessResponse.data.valid === true) {
            // console.log('Access 토큰 유효');
            return await sendRequestWithToken(url, method, data, token);
        } else {
            // console.log('Access 토큰이 만료되었거나 유효하지 않음');

            // Access Token이 만료되면 Refresh Token을 통해 새로운 Access Token을 요청
            const refreshUrl = `http://${window.location.hostname}:8081/users/validate-refresh-token`;
            const refreshResponse = await axios.post(refreshUrl, {
                refreshToken: refreshToken
            });

            if (refreshResponse.data.valid) {
                const newAccessToken = "Bearer " + refreshResponse.data.newAccessToken;
                localStorage.setItem("jwtToken", newAccessToken); // 새로운 Access Token을 로컬 스토리지에 저장
                return await sendRequestWithToken(url, method, data, newAccessToken); // 새로운 Access Token으로 요청 재시도
            } else {
                // console.log('Refresh Token이 만료되었거나 유효하지 않음');
                await handleLogout();
            }
        }
    } catch (error) {
        // console.error('토큰 유효성 확인 실패', error);
        if (error.response && error.response.status === 401) {
            // 4. 로그아웃 처리
            await handleLogout();
        }
    }
}
const handleLogout = async () => {
    window.alert('세션이 만료되었습니다. 다시 로그인해주세요.');

    localStorage.clear();
    cookies.remove('refreshToken');

    await router.push('/');
    location.reload();
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

//프로필이미지 수정
const apiTokenMpt = async (urn, method, data, token) => {
    const url = "http://" + window.location.hostname + ":8081/" + urn
    const refreshToken = cookies.get('refreshToken');

    try {
        // AccessToken 유효성 확인
        const accessUrl = `http://${window.location.hostname}:8081/users/validate-access-token`;
        const accessResponse = await axios.post(accessUrl, null, {
            headers: {
                Authorization: token
            }
        });
        if (accessResponse.data.valid === true) {
            console.log('Access 토큰 유효');
            return await sendRequestWithTokenMpt(url, method, data, token);
        } else {
            console.log('Access 토큰이 만료되었거나 유효하지 않음');

            // Access Token이 만료되면 Refresh Token을 통해 새로운 Access Token을 요청
            const refreshUrl = `http://${window.location.hostname}:8081/users/validate-refresh-token`;
            const refreshResponse = await axios.post(refreshUrl, {
                refreshToken: refreshToken
            });

            if (refreshResponse.data.valid) {
                const newAccessToken = "Bearer " + refreshResponse.data.newAccessToken;
                localStorage.setItem("jwtToken", newAccessToken); // 새로운 Access Token을 로컬 스토리지에 저장
                console.log(newAccessToken);
                return await sendRequestWithTokenMpt(url, method, data, newAccessToken); // 새로운 Access Token으로 요청 재시도
            } else {
                console.log('Refresh Token이 만료되었거나 유효하지 않음');
                await handleLogout();
            }
        }
    } catch (error) {
        console.error('토큰 유효성 확인 실패', error); //  이력관리 수정 오류 순서 3
        if (error.response && error.response.status === 401) {
            // 4. 로그아웃 처리
            //await handleLogout();
        }
    }
}

const loginApi = async (urn, method, data) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    return (await axios({
        url,
        method: method,
        data,
        headers: {
            "Content-Type": "application/json",
            "X-Requested-With": "XMLHttpRequest",
        }
    }).catch(error => {
        console.log('로그인 오류:', error.response.status, error.response.data);
        return error.response;
    }))
}

const sendRequestWithToken = async (url, method, data, token) => { //원래 쓰던 api함수
    try {
        return await axios({ //  이력관리 수정 오류 순서 1
            url,
            method,
            data,
            headers: {
                Authorization: token,
            },
        });
    } catch (error) {
        console.error('호출 오류', error);  //  이력관리 수정 오류 순서 2
        throw error;
    }
};
const sendRequestWithTokenMpt = async (url, method, data, token) => {
    try {
        return await axios({ //  이력관리 수정 오류 순서 1
            url,
            method,
            data,
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: token,
            },
        });
    } catch (error) {
        console.error('호출 오류', error);  //  이력관리 수정 오류 순서 2
        throw error;
    }
};
export {
    api, apiToken, loginApi, api2, apiToken2, apiTokenMpt
};