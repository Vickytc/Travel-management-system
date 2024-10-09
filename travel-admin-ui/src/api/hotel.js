import request from '../util/request.js';

// Query hotel list by paging conditions
export function getHotelPageApi(pageNum, pageSize, hotel) {
    return request({
        method: 'post',
        url: `/hotel/page/${pageNum}/${pageSize}`,
        data: hotel
    });
}

// Add Hotel
export function addHotelApi(data) {
    return request({
        method: 'post',
        url: '/hotel',
        data: data
    });
}

// Delete Hotel
export function deleteHotelApi(id) {
    return request({
        method: 'delete',
        url: `/hotel/${id}`
    });
}

// Modify Hotel
export function updateHotelApi(data) {
    return request({
        method: 'put',
        url: '/hotel',
        data: data
    });
}

// Check hotel details
export function getHotelApi(id) {
    return request({
        method: 'get',
        url: `/hotel/${id}`
    });
}

