<template>
  <el-card v-if="userStore.info.role !== 2">Room Management</el-card>
  <el-card v-else>
    <el-descriptions title="Hotel Details" border>
      <el-descriptions-item :rowspan="2" :width="140" label="Hotel Image" align="center">
        <el-image style="width: 200px; height: 130px" :src="hotelDetail.image"/>
      </el-descriptions-item>
      <el-descriptions-item label="Hotel Name">{{hotelDetail.hotelName}}</el-descriptions-item>
      <el-descriptions-item label="Hotel Description">{{hotelDetail.description}}</el-descriptions-item>
      <el-descriptions-item label="Operating Status">
        <el-tag size="small" :type="hotelDetail.state === 0 ? 'primary' : 'danger'">{{ hotelDetail.state === 0 ? 'Operating Normally' : 'Closed'}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="Official Website">{{hotelDetail.url}}</el-descriptions-item>
      <el-descriptions-item label="Registration Number">{{hotelDetail.businessRegistrationNumber}}</el-descriptions-item>
      <el-descriptions-item label="Contact Person">{{hotelDetail.contactUsername}}</el-descriptions-item>
      <el-descriptions-item label="Contact Email">{{hotelDetail.contactEmail}}</el-descriptions-item>
      <el-descriptions-item label="Contact Phone">
        <el-tag size="small">{{ hotelDetail.contactPhoneNumber }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="Address">
        {{hotelDetail.hotelAddress}}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>

  <!-- Room Display -->
  <el-card style="margin-top: 30px">
    <h4>Hotel Rooms:</h4>
    <!-- Query Form -->
    <el-form :inline="true" :model="queryForm" class="form-inline">
      <el-form-item label="Room Type">
        <el-input v-model.trim="queryForm.roomName" placeholder="Please enter hotel name" clearable />
      </el-form-item>
      <el-form-item label="Room Status">
        <el-select style="width: 200px" v-model="queryForm.state"
                   placeholder="Please select room status" clearable>
          <el-option label="Normal" :value="0" />
          <el-option label="Off the Market" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getRoomList">Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="handleAdd" v-if="userStore.info.role !== 2">Add</el-button>
        </el-form-item>
      </el-form-item>
    </el-form>

    <!-- Display Table -->
    <el-table :data="roomList" style="width: 100%">
      <el-table-column label="Room Type"  prop="roomName">
      </el-table-column>
      <el-table-column label="Room Image" width="300">
        <template #default="scope">
          <el-image style="width: 180px; height: 110px" :src="scope.row.image" fit="fit" />
        </template>
      </el-table-column>
      <el-table-column label="Room Price"  prop="price">
        <template #default="scope">
          <span>{{scope.row.price}} yuan/room</span>
        </template>
      </el-table-column>
      <el-table-column label="Room Stock"  prop="stock">
        <template #default="scope">
          <span>{{scope.row.stock}} rooms</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="primary" v-if="scope.row.state === 0">Normal</el-tag>
            <el-tag type="danger" v-else-if="scope.row.state === 1">Off the Market</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Creation Time"  prop="createTime">
      </el-table-column>

      <el-table-column label="Action" width="220">
        <template #default="scope">
          <el-button type="success" size="small" @click="handleReserve(scope.row)"
                     v-if="userStore.info.role === 2 && hotelDetail.state === 0">Reserve</el-button>
          <el-button type="warning" size="small" @click="handleUpdate(scope.row)" v-if="userStore.info.role !== 2">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)" v-if="userStore.info.role !== 2">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-block">
      <el-pagination
          v-model:current-page="roomCurrentPage"
          v-model:page-size="roomPageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="roomTotal"
          @size-change="handleRoomPageSizeChange"
          @current-change="handleRoomPageNumChange"
      />
    </div>
  </el-card>

  <!-- Comment Display -->
  <el-card style="margin-top: 30px" v-if="userStore.info.role === 2">
    <h4>Related Comments:</h4>
    <!-- Display Table -->
    <el-table :data="commentList" style="width: 100%">
      <el-table-column label="Commenter"  prop="username">
      </el-table-column>
      <el-table-column label="Avatar" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-avatar :size="45" :src="scope.row.image" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Comment Content"  prop="content">
      </el-table-column>
      <el-table-column label="Service Rating"  prop="serviceRating">
        <template #default="scope">
          <el-rate v-model="scope.row.serviceRating" disabled/>
        </template>
      </el-table-column>
      <el-table-column label="Status" >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="warning" v-if="scope.row.state === 0">Unreviewed</el-tag>
            <el-tag type="primary" v-else-if="scope.row.state === 1">Reviewed</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Comment Time"  prop="createTime">
      </el-table-column>
    </el-table>
  </el-card>

  <!-- Add/Edit Dialog -->
  <el-dialog v-model="dialogVisible" :title="dialogFlag ? 'Add Room' : 'Edit Room'" width="750">
    <el-form :model="roomForm" label-width="auto">
      <el-form-item label="Room Type">
        <el-input v-model="roomForm.roomName" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Room Price">
        <el-input-number v-model="roomForm.price" :precision="2" :step="0.1" :max="100000" />
      </el-form-item>
      <el-form-item label="Room Stock">
        <el-input-number v-model="roomForm.stock" :min="1" :max="1000" />
      </el-form-item>
      <el-form-item label="Meal Plan Config">
        <el-card style="width: 100%">
          <el-row>
            <el-col :span="6">
              <!-- Input Key -->
              <el-input v-model="mealPlanName" placeholder="Meal Name" clearable style="width: 90%"/>
            </el-col>
            <el-col :span="14">
              <el-select v-model="mealPlanOptions" multiple filterable allow-create
                         default-first-option :reserve-keyword="false" placeholder="Fill Meal Configuration" style="width: 95%">
              </el-select>
            </el-col>
            <el-col :span="4">
              <!-- Submit Button -->
              <el-button type="primary" @click="addMealPlansConfig">Add</el-button>
            </el-col>
          </el-row>
          <!-- Display Added Meal Configurations -->
          <div v-for="(options, mealPlan) in roomForm.mealPlansConfigMap" :key="mealPlan">
            <p>{{ mealPlan }}:[{{ options.join(', ') }}]
              <el-button type="danger" @click="removeMealPlansConfig(mealPlan)">Delete</el-button></p>
          </div>
        </el-card>
      </el-form-item>
      <el-form-item label="Status" v-if="!dialogFlag">
        <el-select v-model="roomForm.state" placeholder="Select Room Status" style="width: 240px">
          <el-option :key="0" label="Normal" :value="0"/>
          <el-option :key="0" label="Off the Market" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Room Image">
        <el-upload
            action="/api/file/upload"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :file-list="fileList"
            :auto-upload="true"
            :limit="1">
          <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">Cancel</el-button>
        <el-button type="primary" @click="onSubmit">Submit</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- Reservation Dialog -->
  <el-dialog v-model="reserveDialogVisible" title="Reserve Room" width="500">
    <el-form :model="reserveForm" label-width="auto">
      <el-form-item label="Check-in Date">
        <el-date-picker v-model="reserveForm.startDate" type="date" placeholder="Pick a day"/>
      </el-form-item>
      <el-form-item label="Check-out Date">
        <el-date-picker v-model="reserveForm.endDate" type="date" placeholder="Pick a day"/>
      </el-form-item>
      <el-form-item label="Reservation Quantity">
        <el-input-number v-model="reserveForm.count" :min="1" :max="100" />
      </el-form-item>
      <el-form-item prop="code" label="Email Verification Code">
        <el-input :prefix-icon="Right" v-model="reserveForm.code" maxlength="6" placeholder="Email Verification Code" style="width: 80%">
          <template #append>
            <input type="button" :plain="true" @click="getCode()" :disabled="state.show"
                   style="width: 100%;height: 100%;border: 1px;background: none;width: 80px;color: #ababab;"
                   :value="state.codeText" />
          </template>
        </el-input>
      </el-form-item>
      <h4>Meal Plans:</h4>
      <el-form style="margin-left: 30px">
        <el-form-item v-for="(mealPlanList, index) in roomObj.mealPlansConfigMap" :label="index">
          <el-radio-group v-model="mealPlanForm[index]" size="small">
            <el-radio-button v-for="mealPlan in mealPlanList" :label="mealPlan" :value="mealPlan" style="margin-right: 10px"/>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="success" @click="reserveOnSubmit(1)">ON HOLD</el-button>
        <el-button type="primary" @click="reserveOnSubmit(0)">CONFIRMED</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, onMounted, reactive} from "vue";
import {addRoomApi, deleteRoomApi, getRoomPageApi, updateRoomApi} from "@/api/room.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import {useUserStore} from "@/stores/info.js";
import {getHotelApi} from "@/api/hotel.js";
import {getCommentPageApi} from "@/api/comment.js";
import {addReserveApi} from "@/api/reserve.js";
import {Right} from '@element-plus/icons-vue'
import {sendRegisterCodeApi} from "@/api/user.js";

const userStore = useUserStore()
const router = useRouter()
const route = useRoute();
// Get path parameters
const hotelId = route.params.hotelId;

// Query form
const queryForm = reactive({
  roomName: '',
  hotelId: '',
  state: '',
});

// Room data
const roomList = ref([]);
const roomCurrentPage = ref(1); // Page number
const roomPageSize = ref(5); // Number of records displayed per page
const roomTotal = ref(10); // Total number of records
// Get room data
const getRoomList = async () => {
  queryForm.hotelId = hotelId;
  const res = await getRoomPageApi(roomCurrentPage.value, roomPageSize.value, queryForm);
  roomList.value = res.data.records;
  roomTotal.value = res.data.total;
};
// Handle page number change
const handleRoomPageNumChange = (val) => {
  roomCurrentPage.value = val;
  getRoomList();
};
// Handle page size change
const handleRoomPageSizeChange = (val) => {
  roomPageSize.value = val;
  getRoomList();
};

// Control dialog visibility
const dialogVisible = ref(false);
// Flag to identify if dialog is for editing or adding
const dialogFlag = ref(false);
// Edit form
const roomForm = reactive({
  id: '',
  roomName: '',
  image: '',
  price: '',
  stock: '',
  hotelId: '',
  mealPlansConfigMap: {},
  state: '',
});
// Image upload
const fileList = ref([]);
// Upload success response
const handleUploadSuccess = (response, file, fileList) => {
  console.log(response, file, fileList);
  roomForm.image = response.data;
  ElMessage.success('Image uploaded successfully');
};
// Upload failure response
const handleUploadError = (err) => {
  console.error('Image upload failed', err);
  ElMessage.error('Image upload failed');
};
// Clear form content
const clearForm = () => {
  roomForm.id = '';
  roomForm.roomName = '';
  roomForm.image = '';
  roomForm.price = '';
  roomForm.stock = '';
  roomForm.hotelId = '';
  roomForm.mealPlansConfigMap = {};
  roomForm.state = '';
  fileList.value = [];
};
// Handle add response
const handleAdd = () => {
  dialogFlag.value = true;
  dialogVisible.value = true;
  clearForm();
  roomForm.hotelId = hotelId;
};
// Handle update response
const handleUpdate = (row) => {
  dialogFlag.value = false;
  dialogVisible.value = true;
  clearForm();
  roomForm.id = row.id;
  roomForm.roomName = row.roomName;
  roomForm.image = row.image;
  roomForm.price = row.price;
  roomForm.stock = row.stock;
  roomForm.hotelId = row.hotelId;
  roomForm.state = row.state;
  roomForm.mealPlansConfigMap = row.mealPlansConfigMap;
  fileList.value = [];
  fileList.value.push({
    url: row.image,
  });
};
// Handle delete response
const handleDelete = (id) => {
  ElMessageBox.confirm(
      'Are you sure you want to delete this room data?',
      'Friendly Reminder',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    deleteRoomApi(id).then(res => {
      ElMessage({
        type: 'success',
        message: 'Deleted successfully',
      });
      getRoomList();
    });
  }).catch(() => {

  });
}

// Form cancel response
const onCancel = () => {
  dialogVisible.value = false;
};
// Form submission response
const onSubmit = async () => {
  if (dialogFlag.value) {
    addRoomApi(roomForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Added successfully',
      });
      roomCurrentPage.value = Math.ceil((roomTotal.value + 1) / roomPageSize.value);
      getRoomList();
    });
  } else {
    updateRoomApi(roomForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Updated successfully',
      });
      getRoomList();
    });
  }
  dialogVisible.value = false;
}

const mealPlanName = ref(''); // New meal plan
const mealPlanOptions = ref([]); // New meal configuration
// Add new meal configuration item
const addMealPlansConfig = () => {
  if (mealPlanName.value.trim() === '' || mealPlanOptions.value.length === 0) {
    ElMessage.error('Please complete the meal configuration');
    return;
  }

  // Add meal name and options to the object
  roomForm.mealPlansConfigMap[mealPlanName.value] = [...mealPlanOptions.value];

  // Clear input fields
  mealPlanName.value = '';
  mealPlanOptions.value = [];
}

// Delete meal configuration item
const removeMealPlansConfig = (mealPlanKey) => {
  delete roomForm.mealPlansConfigMap[mealPlanKey];
}

// Comment data
const commentList = ref([]);
const commentCurrentPage = ref(1); // Page number
const commentPageSize = ref(5); // Number of records displayed per page
const commentTotal = ref(10); // Total number of records
// Get comment data
const getCommentList = async () => {
  const queryForm = {
    userId: '',
    state: 1,
    hotelId: hotelId,
  };
  const res = await getCommentPageApi(commentCurrentPage.value, commentPageSize.value, queryForm);
  commentList.value = res.data.records;
  commentTotal.value = res.data.total;
}

// Hotel detail data
const hotelDetail = ref('');
// Query hotel details
const getHotelDetail = async () => {
  const res = await getHotelApi(hotelId);
  hotelDetail.value = res.data;
}

// Control reservation dialog visibility
const reserveDialogVisible = ref(false);
// Reservation form
const reserveForm = reactive({
  hotelId: '',
  roomId: '',
  count: '',
  startDate: '',
  endDate: '',
  userId: '',
  state: '',
  mealPlanList: [],
  code: '',
});
// Handle reservation response
const roomObj = ref('');
// Meal preference form data
let mealPlanForm = reactive([]);
const handleReserve = (row) => {
  reserveForm.hotelId = '';
  reserveForm.roomId = '';
  reserveForm.count = '';
  reserveForm.startDate = '';
  reserveForm.endDate = '';
  reserveForm.userId = '';
  reserveForm.state = '';
  reserveForm.mealPlanList = [];
  reserveForm.code = '';
  // Reset mealPlanForm to initial state
  mealPlanForm = reactive({});
  roomObj.value = row;
  // Dynamically generate properties for mealPlanForm based on mealPlansConfigMap
  for (const key in row.mealPlansConfigMap.value) {
    if (!mealPlanForm[key]) {
      mealPlanForm[key] = null; // Initialize to null or another default value
    }
  }
  reserveDialogVisible.value = true;
  reserveForm.hotelId = row.hotelId;
  reserveForm.roomId = row.id;
  reserveForm.userId = userStore.info.id;
}
// Reservation submission response
const reserveOnSubmit = async (state) => {
  // Combine property values in mealPlanForm into an array and sync to roomObj
  if (roomObj.value) {
    // Convert mealPlanForm to the appropriate format and update currentRoomItem
    roomObj.value.mealPlanList = Object.values(mealPlanForm).filter(value => value !== null);
  }
  reserveForm.mealPlanList = roomObj.value.mealPlanList;
  reserveForm.state = state;
  const res = addReserveApi(reserveForm);
  if (res.code === 200) {
    ElMessage.success("Reservation successful, you will receive an email reminder shortly");
  }
  reserveDialogVisible.value = false;
  state.show = false;
  state.codeText = 'Get code';
  state.count = '';
  state.timer = null;
}

// Page state
const state = reactive({
  // Get code click becomes disabled
  show: false,
  // Change text for getting code on click
  codeText: 'Get code',
  // Current seconds
  count: '',
  // Timer
  timer: null,
});

// 60 seconds countdown for getting code
const TIME_COUNT = 60;

// Notify backend to send verification code
const getCode = async () => {
  if (state.show) {
    return; // If timing is in progress, return directly
  }

  // Notify backend to send verification code
  state.show = true; // Start timing
  const res = await sendRegisterCodeApi(userStore.info.email);

  if (res.code === 200) {
    ElMessage.success("Verification code has been sent to your email");
    // Start timer after backend sends successfully
    state.count = TIME_COUNT;
    state.timer = setInterval(() => {
      if (state.count > 0) {
        state.count--;
        state.codeText = state.count + 's';
      } else {
        // Timing ends
        state.show = false;
        clearInterval(state.timer);
        state.timer = null;
        state.codeText = "Get again";
      }
    }, 1000);
  } else {
    ElMessage.error("Failed to send verification code, please try again");
    state.show = false; // Allow resending on failure
  }
};

onMounted(() => {
  getRoomList()
  getCommentList()
  getHotelDetail()
})
</script>

<style scoped>

</style>