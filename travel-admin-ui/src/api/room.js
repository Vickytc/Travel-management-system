import request from '../util/request.js';

// Paginated conditional query for room list
export function getRoomPageApi(pageNum, pageSize, room) {
    return request({
        method: 'post',
        url: `/room/page/${pageNum}/${pageSize}`,
        data: room
    });
}

// Add room
export function addRoomApi(data) {
    return request({
        method: 'post',
        url: '/room',
        data: data
    });
}

// Delete room
export function deleteRoomApi(id) {
    return request({
        method: 'delete',
        url: `/room/${id}`
    });
}

// Update room
export function updateRoomApi(data) {
    return request({
        method: 'put',
        url: '/room',
        data: data
    });
}
