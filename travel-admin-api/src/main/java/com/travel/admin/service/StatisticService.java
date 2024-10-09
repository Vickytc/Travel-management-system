package com.travel.admin.service;

import com.travel.admin.utils.OptionsData;

import java.util.List;
import java.util.Map;


public interface StatisticService {

    Map<String, Integer> getDailyReservationsLastSevenDays();

    Map<String, Integer> getMonthlyReservationsThisYear();

    List<OptionsData> getReservationsByStatus();

    List<Map<String, Object>> getWeeklyAndMonthlyReservationsSummary();

    List<OptionsData> getBestPerformingHotels();

    List<List<Object>> getBestPerformingHotelsRevenue();
}
