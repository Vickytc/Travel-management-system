import request from '../util/request.js';

// User registration
export function userRegisterApi(data) {
    return request({
        method: 'post',
        url: '/user/register',
        data: data
    });
}

// User login
export function userLoginApi(data) {
    return request({
        method: 'post',
        url: '/user/login',
        data: data
    });
}

// Query login information
export function getLoginInfoApi() {
    return request({
        method: 'get',
        url: '/user/loginInfo'
    });
}

// Paginated conditional query for user list
export function getUserPageApi(pageNum, pageSize, user) {
    return request({
        method: 'post',
        url: `/user/page/${pageNum}/${pageSize}`,
        data: user
    });
}

// Add user
export function addUserApi(data) {
    return request({
        method: 'post',
        url: '/user',
        data: data
    });
}

// Delete user
export function deleteUserApi(id) {
    return request({
        method: 'delete',
        url: `/user/${id}`
    });
}

// Update user
export function updateUserApi(data) {
    return request({
        method: 'put',
        url: '/user',
        data: data
    });
}

// Query user list by different roles
export function getUserListByRoleApi(role, state) {
    return request({
        method: 'get',
        url: `/user/list`,
        params: { role, state }
    });
}

// Send registration verification code
export function sendRegisterCodeApi(email) {
    return request({
        method: 'get',
        url: '/user/register/sendCode',
        params: { email }
    });
}
