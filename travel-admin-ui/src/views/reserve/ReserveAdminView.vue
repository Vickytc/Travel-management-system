<template>
  <el-card>Reservation Management</el-card>

  <el-card style="margin-top: 10px">
    <!-- Query Form -->
    <el-form :inline="true" :model="queryForm" class="form-inline">
      <el-form-item label="Reservation ID">
        <el-input v-model.trim="queryForm.id" placeholder="Please enter reservation ID" clearable />
      </el-form-item>
      <el-form-item label="Username">
        <el-input v-model.trim="queryForm.username" placeholder="Please enter username" clearable />
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model.trim="queryForm.email" placeholder="Please enter email" clearable />
      </el-form-item>
      <el-form-item label="Reservation Status">
        <el-select style="width: 200px" v-model="queryForm.state"
                   placeholder="Please select reservation status" clearable>
          <el-option label="Confirmed" :value="0" />
          <el-option label="Pending" :value="1" />
          <el-option label="Cancelled" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getReserveList(userStore.info.id, userStore.info.role)">Search</el-button>
        </el-form-item>
      </el-form-item>
    </el-form>

    <!-- Display Table -->
    <el-table :data="reserveList" style="width: 100%">
      <el-table-column label="Hotel Name" prop="hotelName">
      </el-table-column>
      <el-table-column label="Room Type" prop="roomName">
      </el-table-column>
      <el-table-column label="Room Price" prop="roomPrice">
        <template #default="scope">
          <span>{{scope.row.roomPrice}} yuan/room</span>
        </template>
      </el-table-column>
      <el-table-column label="Reserved Quantity" prop="count">
        <template #default="scope">
          <span>{{scope.row.count}} rooms</span>
        </template>
      </el-table-column>
      <el-table-column label="Check-in Date" prop="startDate">
      </el-table-column>
      <el-table-column label="Check-out Date" prop="endDate">
      </el-table-column>
      <el-table-column label="Total Amount (Including Tax)" prop="totalAmount">
        <template #default="scope">
          <span>{{scope.row.totalAmount}} yuan</span>
        </template>
      </el-table-column>
      <el-table-column label="Status">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="primary" v-if="scope.row.state === 0">Confirmed</el-tag>
            <el-tag type="warning" v-else-if="scope.row.state === 1">Pending</el-tag>
            <el-tag type="danger" v-else-if="scope.row.state === 2">Cancelled</el-tag>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="Action" width="300">
        <template #default="scope">
          <el-button type="warning" size="small" @click="handleGet(scope.row)">Details</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)" v-if="userStore.info.role === 0">Delete</el-button>
          <el-button type="primary" size="small" @click="handleState(scope.row,0)"
                     v-if="scope.row.state === 1 && userStore.info.role !== 1">CONFIRMED</el-button>
          <el-button type="info" size="small" @click="handleState(scope.row, 2)"
                     v-if="scope.row.state === 0 && userStore.info.role !== 1">Cancel</el-button>
          <el-button type="success" size="small" @click="handleComment(scope.row)"
                     v-if="scope.row.state === 0 && userStore.info.role === 2 && !scope.row.commentId">Comment</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-block">
      <el-pagination
          v-model:current-page="reserveCurrentPage"
          v-model:page-size="reservePageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="reserveTotal"
          @size-change="handleReservePageSizeChange"
          @current-change="handleReservePageNumChange"
      />
    </div>
  </el-card>

  <el-dialog v-model="dialogVisible" title="Reservation Details" width="750">
    <el-form label-width="auto">
      <el-form-item label="Hotel Name">
        <el-input v-model="reserve.hotelName" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Room Type">
        <el-input v-model="reserve.roomName" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Room Price">
        <el-input-number v-model="reserve.roomPrice" :precision="2" :step="0.1" :max="100000" disabled/>
      </el-form-item>
      <el-form-item label="Reserved Quantity">
        <el-input-number v-model="reserve.count" :min="1" :max="1000" disabled/>
      </el-form-item>
      <el-form-item label="Check-in Date">
        <el-input v-model="reserve.startDate" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Check-out Date">
        <el-input v-model="reserve.endDate" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Meal Plan">
        <el-card style="width: 100%">
          <el-tag v-for="meal in reserve.mealPlanList" type="primary" style="margin-right: 10px">{{meal}}</el-tag>
        </el-card>
      </el-form-item>
      <el-form-item label="Reserved By">
        <el-input v-model="reserve.username" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="reserve.email" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Total Amount">
        <el-input v-model="reserve.totalAmount" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="Creation Date">
        <el-input v-model="reserve.createTime" autocomplete="off" disabled/>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">Cancel</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="commentDialogVisible" title="Comment" width="750">
    <el-form label-width="auto">
      <el-form-item label="Comment Content">
        <el-input type="textarea" :rows="3" v-model="commentForm.content" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Service Rating">
        <el-rate v-model="commentForm.serviceRating" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="commentDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="onSubmit">Submit</el-button>
      </div>
    </template>
  </el-dialog>
</template>


<script setup>
import {ref, onMounted, reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import {useUserStore} from "@/stores/info.js";
import {deleteReserveApi, getReserveAllByUserIdApi, getReservePageApi, updateReserveApi} from "@/api/reserve.js";
import {addCommentApi} from "@/api/comment.js";
import {getLoginInfoApi} from "@/api/user.js";

const userStore = useUserStore()
const router = useRouter()
const route = useRoute();

// Query Form
const queryForm = reactive({
  id: "",
  username: "",
  userId: "",
  email: "",
  state: ""
})

// Reservation Data
const reserveList = ref([])
const reserveCurrentPage = ref(1) // Page Number
const reservePageSize = ref(5) // Number of records displayed per page
const reserveTotal = ref(10) // Total number of records
// Get Reservation Data
const getReserveList = async (id, role) => {
  if (role === 1) {
    // Query reservation list of hotels under own name
    const result = await getReserveAllByUserIdApi(id ? id : userStore.info.id, reserveCurrentPage.value, reservePageSize.value)
    reserveList.value = result.data.records
    reserveTotal.value = result.data.total;
    return;
  } else if (role === 2) {
    queryForm.userId = userStore.info.id
  }
  const res = await getReservePageApi(reserveCurrentPage.value, reservePageSize.value, queryForm)
  reserveList.value = res.data.records;
  reserveTotal.value = res.data.total;
}
// Handle Page Number Change
const handleReservePageNumChange = (val) => {
  reserveCurrentPage.value = val
  getReserveList(userStore.info.id, userStore.info.role);
}
// Handle Page Size Change
const handleReservePageSizeChange = (val) => {
  reservePageSize.value = val
  getReserveList(userStore.info.id, userStore.info.role);
}

// Control Dialog Visibility
const dialogVisible = ref(false)
// Edit Form
const reserveForm = reactive({
  id: '',
  hotelId: '',
  roomId: '',
  count: '',
  startDate: '',
  endDate: '',
  userId: '',
  state: '',
  mealPlanList: [],
})
// Clear Form Content
const clearForm = () => {
  reserveForm.id = ''
  reserveForm.hotelId = ''
  reserveForm.roomId = ''
  reserveForm.count = ''
  reserveForm.startDate = ''
  reserveForm.endDate = ''
  reserveForm.userId = ''
  reserveForm.state = ''
  reserveForm.mealPlanList = []
}
// Handle Details
const reserve = ref('')
const handleGet = (row) => {
  dialogVisible.value = true
  reserve.value = row
}

// Handle Confirm and Cancel
const handleState = async (row, state) => {
  clearForm()
  reserveForm.id = row.id
  reserveForm.hotelId = row.hotelId
  reserveForm.roomId = row.roomId
  reserveForm.count = row.count
  reserveForm.startDate = row.startDate
  reserveForm.endDate = row.endDate
  reserveForm.userId = row.userId
  reserveForm.state = state
  reserveForm.mealPlanList = row.mealPlanList
  await updateReserveApi(reserveForm).then(res => {
    ElMessage({
      type: 'success',
      message: 'Update successful',
    })
    getReserveList(userStore.info.id, userStore.info.role);
  })
}

// Handle Delete Response
const handleDelete = (id) => {
  ElMessageBox.confirm(
      'Are you sure you want to delete this reservation data?',
      'Friendly Reminder',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    deleteReserveApi(id).then(res => {
      ElMessage({
        type: 'success',
        message: 'Delete successful',
      })
      getReserveList(userStore.info.id, userStore.info.role);
    })
  }).catch(() => {

  })
}

// Control Comment Dialog Visibility
const commentDialogVisible = ref(false)
// Comment Form
const commentForm = reactive({
  reserveId: '',
  content: '',
  serviceRating: '',
  hotelId: '',
  roomId: '',
  userId: '',
})
// Handle Comment Response
const handleComment = (row) => {
  commentForm.content = ''
  commentForm.serviceRating = ''
  commentForm.hotelId = ''
  commentForm.roomId = ''
  commentForm.userId = ''
  commentDialogVisible.value = true
  commentForm.reserveId = row.id
  commentForm.hotelId = row.hotelId
  commentForm.roomId = row.roomId
  commentForm.userId = userStore.info.id
}
// Comment Form Submission Response
const onSubmit = async () => {
  console.log(commentForm.reserveId)
  await addCommentApi(commentForm, commentForm.reserveId).then(res => {
    ElMessage({
      type: 'success',
      message: 'Comment successful',
    })
    getReserveList(userStore.info.id, userStore.info.role);
  })
  commentDialogVisible.value = false
}

// Form Cancel Response
const onCancel = () => {
  dialogVisible.value = false
}

onMounted(async () => {
  const res = await getLoginInfoApi();
  await getReserveList(res.data.id, res.data.role)
})
</script>

<style scoped>

</style>