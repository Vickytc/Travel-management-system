import axios from "axios";
import { ElMessage } from "element-plus";
import router from "../router";

const baseURL = '/api';

axios.defaults.headers.post['Content-Type'] = 'application/json';
const instance = axios.create({
    baseURL
});

instance.interceptors.response.use(
    result => {
        // Response successful
        if (result.data.code === 200) {
            return result.data;
        }
        ElMessage.error(result.data.message || 'Service exception');
        return Promise.reject(result.data);
    },
    error => {
        // Response failed, status code not in 2xx
        if (error.response.status == 401) {
            ElMessage.error('You are not logged in');
            router.push('/login');
        } else {
            ElMessage.error('Service exception');
        }
        return Promise.reject(error);
    }
);

instance.interceptors.request.use(
    config => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = token;
        }
        return config;
    },
    error => {
        Promise.reject(error);
    }
);

export default instance;
