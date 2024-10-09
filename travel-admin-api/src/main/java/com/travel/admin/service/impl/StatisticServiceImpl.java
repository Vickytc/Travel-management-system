package com.travel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.mapper.HotelMapper;
import com.travel.admin.mapper.ReserveMapper;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.service.StatisticService;
import com.travel.admin.utils.OptionsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final ReserveMapper reserveMapper;
    private final HotelMapper hotelMapper;

    @Override
    public Map<String, Integer> getDailyReservationsLastSevenDays() {
        // Get today's date
        Date today = new Date();

        // Create a calendar object, set time to today, subtract seven days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();

        // Query all reservation counts for the last seven days, including today
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(Reserve::getCreateTime, sevenDaysAgo).le(Reserve::getCreateTime, today);
        List<Reserve> reserveList = reserveMapper.selectList(queryWrapper);

        // Group and count by each day's date
        Map<String, Integer> dailyCountMap = new HashMap<>();
        for (Reserve reserve : reserveList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(reserve.getCreateTime());
            String dateKey = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

            dailyCountMap.put(dateKey, dailyCountMap.getOrDefault(dateKey, 0) + 1);
        }

        // Ensure each day has an entry and maintain ascending order
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_YEAR, -i);
            String dateKey = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

            // If there is no record for that date, initialize to 0
            dailyCountMap.putIfAbsent(dateKey, 0);
        }

        // Create a sorted LinkedHashMap
        Map<String, Integer> sortedDailyCountMap = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(dailyCountMap.keySet());
        keys.sort(Comparator.comparing(date -> {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }));

        // Insert sorted key-value pairs into the ordered map
        for (String key : keys) {
            sortedDailyCountMap.put(key, dailyCountMap.get(key));
        }

        // Return result
        return sortedDailyCountMap;
    }

    @Override
    public Map<String, Integer> getMonthlyReservationsThisYear() {
        // Get the current year
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // Query all reservation data for this year
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(Reserve::getCreateTime, LocalDate.of(year, 1, 1))
                .le(Reserve::getCreateTime, LocalDate.of(year, 12, 31));
        List<Reserve> reserveList = reserveMapper.selectList(queryWrapper);

        // Use LinkedHashMap to count each month's quantities
        Map<String, Integer> monthlyCountMap = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            String monthKey = month + "月";
            monthlyCountMap.put(monthKey, 0);  // Initialize each month's quantity to 0
        }

        // Count reservations for each month
        for (Reserve reserve : reserveList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(reserve.getCreateTime());
            int month = cal.get(Calendar.MONTH) + 1;  // Month starts from 0
            String monthKey = month + "月";

            monthlyCountMap.put(monthKey, monthlyCountMap.get(monthKey) + 1);
        }

        // Return result
        return monthlyCountMap;
    }

    @Override
    public List<OptionsData> getReservationsByStatus() {
        // Query the count of reservations with status CONFIRMED and ON_HOLD
        long confirmedCount = reserveMapper.selectCount(new QueryWrapper<Reserve>()
                .lambda()
                .eq(Reserve::getState, StateConstant.CONFIRMED));

        long onHoldCount = reserveMapper.selectCount(new QueryWrapper<Reserve>()
                .lambda()
                .eq(Reserve::getState, StateConstant.ON_HOLD));

        // Create result list and return
        List<OptionsData> optionsData = new ArrayList<>();
        optionsData.add(new OptionsData("CONFIRMED", confirmedCount));
        optionsData.add(new OptionsData("ONHOLD", onHoldCount));

        return optionsData;
    }

    @Override
    public List<Map<String, Object>> getWeeklyAndMonthlyReservationsSummary() {
        // Get the current date
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();

        // Get the Monday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date monday = calendar.getTime();
        if (monday.after(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            monday = calendar.getTime();
        }
        // Get the Sunday of the current week
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date sunday = calendar.getTime();
        if (sunday.before(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            sunday = calendar.getTime();
        }
        // Get the dates of last Monday and Sunday
        calendar = Calendar.getInstance();
        calendar.setTime(monday);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date lastMonday = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.setTime(sunday);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date lastSunday = calendar.getTime();
        // Query reservation data for this week
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(Reserve::getCreateTime, monday).le(Reserve::getCreateTime, sunday);
        List<Reserve> currentWeekReservations = reserveMapper.selectList(queryWrapper);

        // Query reservation data for last week
        queryWrapper.clear(); // Clear previous query conditions
        queryWrapper.lambda().ge(Reserve::getCreateTime, lastMonday).le(Reserve::getCreateTime, lastSunday);
        List<Reserve> lastWeekReservations = reserveMapper.selectList(queryWrapper);

        // Initialize daily reservation count arrays
        int[] currentWeekData = new int[7]; // Daily reservation count for this week
        int[] lastWeekData = new int[7]; // Daily reservation count for last week

        // Count daily reservations for this week
        for (Reserve reserve : currentWeekReservations) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(reserve.getCreateTime());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY; // Monday is 0
            if (dayOfWeek >= 0 && dayOfWeek < 7) {
                currentWeekData[dayOfWeek]++;
            }
        }

        // Count daily reservations for last week
        for (Reserve reserve : lastWeekReservations) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(reserve.getCreateTime());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY; // Monday is 0
            if (dayOfWeek >= 0 && dayOfWeek < 7) {
                lastWeekData[dayOfWeek]++;
            }
        }

        // Build return data
        List<Map<String, Object>> result = new ArrayList<>();

        // This week
        Map<String, Object> currentWeekSummary = new HashMap<>();
        currentWeekSummary.put("name", "This Week");
        currentWeekSummary.put("type", "line");
        currentWeekSummary.put("stack", "Total");
        currentWeekSummary.put("data", Arrays.stream(currentWeekData).boxed().collect(Collectors.toList()));

        // Last week
        Map<String, Object> lastWeekSummary = new HashMap<>();
        lastWeekSummary.put("name", "Last Week");
        lastWeekSummary.put("type", "line");
        lastWeekSummary.put("stack", "Total");
        lastWeekSummary.put("data", Arrays.stream(lastWeekData).boxed().collect(Collectors.toList()));

        result.add(currentWeekSummary);
        result.add(lastWeekSummary);

        return result;
    }

    @Override
    public List<OptionsData> getBestPerformingHotels() {
        // Query all reservation data
        List<Reserve> reserveList = reserveMapper.selectList(null);
        // Group by Reserve.getHotelId and calculate the number of reservations for each hotelId
        Map<Integer, Long> hotelReservationCount = reserveList.stream()
                .collect(Collectors.groupingBy(Reserve::getHotelId, Collectors.counting()));
        // Sort and take the top five hotels in descending order
        List<Map.Entry<Integer, Long>> sortedHotelEntries = hotelReservationCount.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(5)
                .collect(Collectors.toList());
        // Build return result
        List<OptionsData> bestPerformingHotels = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : sortedHotelEntries) {
            Integer hotelId = entry.getKey();
            Long count = entry.getValue();
            String hotelName = getHotelNameById(hotelId); // Assume there is a method to get hotel name by hotelId
            bestPerformingHotels.add(new OptionsData(hotelName, count));
        }

        return bestPerformingHotels;
    }

    @Override
    public List<List<Object>> getBestPerformingHotelsRevenue() {
        // Query all reservation data
        List<Reserve> reserveList = reserveMapper.selectList(null);
        // Group by Reserve.getHotelId and calculate total revenue for each hotelId
        Map<Integer, BigDecimal> hotelRevenueMap = reserveList.stream()
                .collect(Collectors.groupingBy(
                        Reserve::getHotelId,
                        Collectors.reducing(BigDecimal.ZERO, Reserve::getTotalAmount, BigDecimal::add) // Calculate total revenue
                ));
        // Sort and take the top five hotels (sorted by total revenue in ascending order)
        List<Map.Entry<Integer, BigDecimal>> topFiveHotels = hotelRevenueMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .collect(Collectors.toList());
        List<List<Object>> result = topFiveHotels.stream()
                .map(entry -> {
                    String hotelName = getHotelNameById(entry.getKey());
                    // Return a list containing hotel name and total revenue
                    List<Object> hotelData = new ArrayList<>();
                    hotelData.add(hotelName);
                    hotelData.add(entry.getValue().intValue()); // Convert BigDecimal to Integer
                    return hotelData;
                })
                .collect(Collectors.toList());

        return result;
    }

    private String getHotelNameById(Integer hotelId) {
        return hotelMapper.selectById(hotelId).getHotelName();
    }
}
