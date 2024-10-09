package com.travel.admin.controller;

import com.travel.admin.model.Result;
import com.travel.admin.service.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Statistical chart-related API
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Resource
    private StatisticService statisticService;

    /**
     * Query the daily reservation volume for the past seven days
     *
     * @return A list of daily reservation volumes
     */
    @GetMapping("/reservations/last-seven-days")
    public Result getDailyReservationsLastSevenDays() {
        return Result.success(statisticService.getDailyReservationsLastSevenDays());
    }

    /**
     * Query the total reservation volume for each month of the current year
     *
     * @return A list of total reservation volumes for each month
     */
    @GetMapping("/reservations/this-year")
    public Result getMonthlyReservationsThisYear() {
        return Result.success(statisticService.getMonthlyReservationsThisYear());
    }

    /**
     * Query the volume of retained and confirmed reservations
     *
     * @return The volume of retained and confirmed reservations
     */
    @GetMapping("/reservations/status")
    public Result getReservationsByStatus() {
        return Result.success(statisticService.getReservationsByStatus());
    }

    /**
     * Query the total reservation volume for this week, last week, and this month
     *
     * @return Total reservation volumes for each time period
     */
    @GetMapping("/reservations/summary")
    public Result getWeeklyAndMonthlyReservationsSummary() {
        return Result.success(statisticService.getWeeklyAndMonthlyReservationsSummary());
    }

    /**
     * Query the top five performing hotels
     *
     * @return A list of the top five performing hotels
     */
    @GetMapping("/hotels/best-performing")
    public Result getBestPerformingHotels() {
        return Result.success(statisticService.getBestPerformingHotels());
    }

    /**
     * Query the total revenue of the top five performing hotels
     *
     * @return The total revenue of the best hotels
     */
    @GetMapping("/hotels/best-performing/revenue")
    public Result getBestPerformingHotelsRevenue() {
        return Result.success(statisticService.getBestPerformingHotelsRevenue());
    }
}

