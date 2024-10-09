import request from '../util/request.js';

// Add comment
export function addCommentApi(data, reserveId) {
    return request({
        method: 'post',
        url: '/comment',
        data: data,
        params: { reserveId }
    });
}

// Delete comment
export function deleteCommentApi(id) {
    return request({
        method: 'delete',
        url: `/comment/${id}`
    });
}

// Update comment
export function updateCommentApi(data) {
    return request({
        method: 'put',
        url: '/comment',
        data: data
    });
}

// Paginated query for comment list
export function getCommentPageApi(pageNum, pageSize, params) {
    return request({
        method: 'post',
        url: `/comment/page/${pageNum}/${pageSize}`,
        data: params
    });
}

// View all reviews for your hotel
export function getCommentAllByUserIdApi(userId, pageNum, pageSize) {
    return request({
        method: 'get',
        url: `/comment/page/all/${userId}/${pageNum}/${pageSize}`,
    });
}