<template>
  <!-- First Row -->
  <el-row>
    <!-- Query daily reservations for the last seven days -->
    <el-col :span="12">
      <h2>Daily Reservations in the Last Seven Days</h2>
      <div id="dailyReservationsLastSevenDays" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
    <!-- Query total reservations for each month this year -->
    <el-col :span="12">
      <h2>Total Reservations for Each Month This Year</h2>
      <div id="monthlyReservationsThisYear" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
  </el-row>
  <!-- Second Row -->
  <el-row>
    <!-- Query reservations by customer retention and confirmation status -->
    <el-col :span="12">
      <h2>Customer Retained and Confirmed Reservations</h2>
      <div id="reservationsByStatus" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
    <!-- Query total reservations for this week, last week, and this month -->
    <el-col :span="12">
      <h2>Total Reservations for This Week and Last Week</h2>
      <div id="weeklyAndMonthlyReservationsSummary" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
  </el-row>
  <!-- Third Row -->
  <el-row>
    <!-- Query the top five performing hotels -->
    <el-col :span="12">
      <h2>Top Five Performing Hotels</h2>
      <div id="bestPerformingHotels" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
    <!-- Query total revenue of the top five performing hotels -->
    <el-col :span="12">
      <h2>Total Revenue of the Top Five Performing Hotels</h2>
      <div id="bestPerformingHotelsRevenue" :style="{ width: '100%', height: '400px' }"></div>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from "echarts";
import {
  getBestPerformingHotelsApi, getBestPerformingHotelsRevenueApi,
  getDailyReservationsLastSevenDaysApi,
  getMonthlyReservationsThisYearApi,
  getReservationsByStatusApi, getWeeklyAndMonthlyReservationsSummaryApi
} from "@/api/statistic.js";

let echart = echarts;

// Query daily reservations for the last seven days
const getDailyReservationsLastSevenDays = async () => {
  const res = await getDailyReservationsLastSevenDaysApi()
  var chart = echart.init(document.getElementById("dailyReservationsLastSevenDays"));

  chart.setOption({
    xAxis: {
      type: 'category',
      data: Object.keys(res.data)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(res.data),
        type: 'bar'
      }
    ]
  })
  window.onresize = function () {
    // Responsive size
    chart.resize();
  };
}

// Query total reservations for each month this year
const getMonthlyReservationsThisYear = async () => {
  const res = await getMonthlyReservationsThisYearApi()
  var chart = echart.init(document.getElementById("monthlyReservationsThisYear"));

  chart.setOption({
    xAxis: {
      type: 'category',
      data: Object.keys(res.data)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(res.data),
        type: 'line',
        smooth: true
      }
    ]
  })
}

// Query reservations by customer retention and confirmation status
const getReservationsByStatus = async () => {
  const res = await getReservationsByStatusApi()
  var chart = echart.init(document.getElementById("reservationsByStatus"));
  chart.setOption({
    title: {
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: res.data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

// Query total reservations for this week, last week, and this month
const getWeeklyAndMonthlyReservationsSummary = async () => {
  const res = await getWeeklyAndMonthlyReservationsSummaryApi()
  var chart = echart.init(document.getElementById("weeklyAndMonthlyReservationsSummary"));

  chart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: res.data
  })
}

// Query the top five performing hotels
const getBestPerformingHotels = async () => {
  const res = await getBestPerformingHotelsApi()
  var chart = echart.init(document.getElementById("bestPerformingHotels"));

  chart.setOption({
    legend: {
      top: 'bottom'
    },
    toolbox: {
      show: true,
      feature: {
        mark: { show: true },
        dataView: { show: true, readOnly: false },
        restore: { show: true },
        saveAsImage: { show: true }
      }
    },
    series: [
      {
        name: 'Nightingale Chart',
        type: 'pie',
        center: ['50%', '50%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 8
        },
        data: res.data
      }
    ]
  })
}

// Query total revenue of the top five performing hotels
const getBestPerformingHotelsRevenue = async () => {
  const res = await getBestPerformingHotelsRevenueApi()
  var chart = echart.init(document.getElementById("bestPerformingHotelsRevenue"));

  chart.setOption({
    dataset: [
      {
        dimensions: ['hotelName','totalAmount'],
        source: res.data
      },
      {
        transform: {
          type: 'sort',
          config: { dimension: 'totalAmount', order: 'asc' }
        }
      }
    ],
    xAxis: {
      type: 'category',
      axisLabel: { interval: 0, rotate: 20 }
    },
    yAxis: {},
    series: {
      type: 'bar',
      encode: { x: 'hotelName', y: 'totalAmount' },
      datasetIndex: 1
    }
  })
}

onMounted(() => {
  getDailyReservationsLastSevenDays()
  getMonthlyReservationsThisYear()
  getReservationsByStatus()
  getWeeklyAndMonthlyReservationsSummary()
  getBestPerformingHotels()
  getBestPerformingHotelsRevenue()
});

onUnmounted(() => {
  echart.dispose();
});
</script>

<style scoped>

</style>
