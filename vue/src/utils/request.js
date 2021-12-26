import axios from 'axios'
import router from "../router";

const request = axios.create({
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    if (localStorage.getItem('Authorization')) {
        config.headers.Authorization = localStorage.getItem('Authorization');
    }
    return config;
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        if(res === 'failed'){
            router.push("/login")
            alert("你没有登录")
            return Promise.reject(error)
        }
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }

)


export default request

