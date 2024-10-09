import request from '../util/request.js';

// Query daily reservations for the last seven days
export function getDailyReservationsLastSevenDaysApi() {
    return request({
        method: 'get',
        url: '/statistic/reservations/last-seven-days'
    });
}

// Query total reservations for each month this year
export function getMonthlyReservationsThisYearApi() {
    return request({
        method: 'get',
        url: '/statistic/reservations/this-year'
    });
}

// Query the number of customer-held and confirmed reservations
export function getReservationsByStatusApi() {
    return request({
        method: 'get',
        url: '/statistic/reservations/status'
    });
}

// Query total reservations for this week and last week
export function getWeeklyAndMonthlyReservationsSummaryApi() {
    return request({
        method: 'get',
        url: '/statistic/reservations/summary'
    });
}

// Query the top five performing hotels
export function getBestPerformingHotelsApi() {
    return request({
        method: 'get',
        url: '/statistic/hotels/best-performing'
    });
}

// Query total revenue of the top five performing hotels
export function getBestPerformingHotelsRevenueApi() {
    return request({
        method: 'get',
        url: '/statistic/hotels/best-performing/revenue'
    });
}
