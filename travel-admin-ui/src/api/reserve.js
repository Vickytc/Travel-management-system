import request from '../util/request.js';

// Add reservation
export function addReserveApi(data) {
    return request({
        method: 'post',
        url: '/reserve',
        data: data
    });
}

// Delete reservation
export function deleteReserveApi(id) {
    return request({
        method: 'delete',
        url: `/reserve/${id}`
    });
}

// Update reservation
export function updateReserveApi(data) {
    return request({
        method: 'put',
        url: '/reserve',
        data: data
    });
}

// Paginated query for reservation list
export function getReservePageApi(pageNum, pageSize, params) {
    return request({
        method: 'post',
        url: `/reserve/page/${pageNum}/${pageSize}`,
        data: params
    });
}

// Query all reservations under the hotel of the merchant
export function getReserveAllByUserIdApi(userId, pageNum, pageSize) {
    return request({
        method: 'get',
        url: `/reserve/page/all/${userId}/${pageNum}/${pageSize}`,
    });
}
