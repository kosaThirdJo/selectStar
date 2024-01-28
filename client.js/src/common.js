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
const apiRequest = async (urn, method, data, token) => {
    const url = "http://"+  window.location.hostname + ":8081/" +  urn
    const accessToken = localStorage.getItem("jwtToken");
    // AccessToken 유효성 확인
    axios
        .post('/users/validate-token', accessToken, {
            headers: {
                Authorization: token
            }
        })
        .then(async response => {
            console.log(response);
            // if (response.data.valid) {
            //     console.log('토큰이 유효합니다.');
            //     return await sendRequestWithToken(url, method, data, token);
            // } else {
            //     console.log('토큰이 만료되었거나 유효하지 않습니다.');
            // }
        })
        // .catch(error => {
        //     console.log(error);
        //     console.error('토큰 유효성 확인에 실패했습니다.', error);
        // });


}

const sendRequestWithToken = async (url, method, data, token) => {
    try {
        return await axios({
            url,
            method,
            data,
            headers: {
                Authorization: token,
            },
        });
    } catch (error) {
        console.error('호출 오류', error);
        throw error;
    }
};
export {
    api, apiToken, loginApi, api2, apiToken2, apiRequest
};