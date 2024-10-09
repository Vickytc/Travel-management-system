<template>
  <!-- Conditional Search Box -->
  <el-card style="margin-bottom: 10px;text-align: center">
    <el-form :inline="true" :model="queryForm" style="margin-bottom: -20px">
      <el-form-item label="Hotel Name">
        <el-input v-model.trim="queryForm.hotelName" placeholder="Please enter hotel name" clearable />
      </el-form-item>
      <el-form-item label="Hotel Star Rating">
        <el-select style="width: 200px" v-model="queryForm.starRating"
                   placeholder="Please select hotel star rating" clearable>
          <el-option label="One Star" :value="1" />
          <el-option label="Two Stars" :value="2" />
          <el-option label="Three Stars" :value="3" />
          <el-option label="Four Stars" :value="4" />
          <el-option label="Five Stars" :value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="Hotel Address">
        <el-input v-model.trim="queryForm.hotelAddress" placeholder="Please enter hotel address" clearable />
      </el-form-item>
      <el-form-item>
        <el-form-item>
          <el-button type="warning" @click="getHotelList" style="width: 100px">Search</el-button>
        </el-form-item>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- Hotel Display -->
  <el-row v-for="(row, index) in hotelRows" :key="index" :gutter="20" style="margin-bottom: 20px">
    <el-col :span="1"></el-col>
    <el-col :span="5" v-for="hotel in row" :key="hotel.id">
      <el-card shadow="hover" style="text-align: center">
        <img style="width: 100%;height: 180px;" :src="hotel.image" />
        <h3>{{ hotel.hotelName }}</h3>
        <el-text truncated>{{ hotel.description }}</el-text><br/>
        <el-text truncated><el-rate v-model="hotel.starRating" disabled/></el-text><br/>
        <el-button type="primary" style="margin-top: 5px;width: 100%" @click="handleGet(hotel)">Details</el-button>
      </el-card>
    </el-col>
    <el-col :span="1"></el-col>
  </el-row>
  <!-- Pagination -->
  <el-row style="margin-bottom: 20px;margin-top: 20px">
    <el-col :span="11"></el-col>
    <el-col :span="8" style="text-align: center">
      <el-pagination size="large" background layout="prev, pager, next" :total="hotelTotal" @current-change="handleHotelPageNumChange"
                     :hide-on-single-page="hotelList.length <= 10"/>
    </el-col>
    <el-col :span="5"></el-col>
  </el-row>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from "vue";
import { getUserListByRoleApi } from "@/api/user.js";
import { getHotelPageApi } from "@/api/hotel.js";
import { useRouter } from "vue-router";

const router = useRouter()

// Query Form
const queryForm = reactive({
  hotelName: '',
  starRating: '',
  hotelAddress: '',
  userId: ''
})

// Hotel User Data
const userList = ref([])
// Query Hotel User List
const getHotelUserList = async () => {
  const res = await getUserListByRoleApi(1, 0)
  userList.value = res.data
}

// Hotel Data
const hotelList = ref([])
const hotelCurrentPage = ref(1) // Page Number
const hotelPageSize = ref(10)
const hotelTotal = ref(10) // Total Record Count
// Get Hotel Data
const getHotelList = async () => {
  const res = await getHotelPageApi(hotelCurrentPage.value, hotelPageSize.value, queryForm)
  hotelList.value = res.data.records;
  hotelTotal.value = res.data.total;
}
// Handle Page Number Change
const handleHotelPageNumChange = (val) => {
  hotelCurrentPage.value = val
  getHotelList();
}

// Computed Property to Group Hotel List into Rows of Four
const hotelRows = computed(() => {
  const rows = [];
  const itemsPerRow = 4;
  for (let i = 0; i < hotelList.value.length; i += itemsPerRow) {
    rows.push(hotelList.value.slice(i, i + itemsPerRow));
  }
  return rows;
});

// Handle Details
const handleGet = (row) => {
  router.push("/room_admin/" + row.id);
}


onMounted(() => {
  getHotelList()
  getHotelUserList()
})
</script>

<style scoped>

</style>