<template>
  <el-card>Hotel Management</el-card>

  <el-card style="margin-top: 10px">
    <!-- Query Form -->
    <el-form :inline="true" :model="queryForm" class="form-inline">
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
          <el-button type="primary" @click="getHotelList">Query</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="handleAdd">Add</el-button>
        </el-form-item>
        <el-form-item v-if="userStore.info.role === 0">
          <el-button type="danger" @click="handleExport">Export</el-button>
        </el-form-item>
        <el-form-item v-if="userStore.info.role === 0">
          <el-upload
              style="margin-top: 3px"
              action="/api/hotel/import"
              list-type="text"
              :on-success="handleImportSuccess"
              :on-error="handleImportError"
              :show-file-list="false"
              :file-list="excelFileList"
              :auto-upload="true"
              :accept="'.xlsx,.xls'"
              ref="upload"
              :limit="1">
            <el-button type="info">Import</el-button>
          </el-upload>
        </el-form-item>
      </el-form-item>
    </el-form>

    <!-- Display Form -->
    <el-table :data="hotelList" style="width: 100%">
      <el-table-column label="Hotel Name" prop="hotelName">
      </el-table-column>
      <el-table-column label="Hotel Image" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-image style="width: 180px; height: 110px" :src="scope.row.image" fit="fit" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Description" prop="description">
      </el-table-column>
      <el-table-column label="Star Rating" prop="starRating">
        <template #default="scope">
          <el-rate v-model="scope.row.starRating" disabled/>
        </template>
      </el-table-column>
      <el-table-column label="Official Website" prop="url">
      </el-table-column>
      <el-table-column label="Status">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="primary" v-if="scope.row.state === 0">Open for Business</el-tag>
            <el-tag type="warning" v-else-if="scope.row.state === 1">Closed</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Creation Time" prop="createTime">
      </el-table-column>

      <el-table-column label="Actions" width="300">
        <template #default="scope">
          <el-button type="success" size="small" @click="handleToRoomAdmin(scope.row.id)">Room Admin</el-button>
          <el-button type="warning" size="small" @click="handleUpdate(scope.row)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-block">
      <el-pagination
          v-model:current-page="hotelCurrentPage"
          v-model:page-size="hotelPageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="hotelTotal"
          @size-change="handleHotelPageSizeChange"
          @current-change="handleHotelPageNumChange"
      />
    </div>
  </el-card>

  <!-- Add/Edit Dialog -->
  <el-dialog v-model="dialogVisible" :title="dialogFlag ? 'Add Hotel' : 'Edit Hotel'" width="500">
    <el-form :model="hotelForm" label-width="auto">
      <el-form-item label="Hotel Name">
        <el-input v-model="hotelForm.hotelName" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Address">
        <el-input v-model="hotelForm.hotelAddress" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Star Rating">
        <el-rate v-model="hotelForm.starRating"/>
      </el-form-item>
      <el-form-item label="Official Website">
        <el-input v-model.trim="hotelForm.url" placeholder="Please enter hotel official website"/>
      </el-form-item>
      <el-form-item label="Description">
        <el-input type="textarea" :rows="2"  v-model.trim="hotelForm.description" placeholder="Please enter description"/>
      </el-form-item>
      <el-form-item label="User" v-if="userStore.info.role === 0">
        <el-select v-model="hotelForm.userId" placeholder="Select User" style="width: 240px">
          <el-option v-for="user in userList" :key="user.id" :label="user.username" :value="user.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Status" v-if="!dialogFlag">
        <el-select v-model="hotelForm.state" placeholder="Select Hotel Status" style="width: 240px">
          <el-option :key="0" label="Open for Business" :value="0"/>
          <el-option :key="0" label="Closed" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Hotel Image">
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
</template>

<script setup>
import {ref, onMounted, reactive} from "vue";
import {addHotelApi, deleteHotelApi, getHotelPageApi, updateHotelApi} from "@/api/hotel.js";
import {useUserStore} from "@/stores/info.js";
import {ElMessage, ElMessageBox} from "element-plus";
import axios from "axios";
import {useRouter} from "vue-router";
import {getLoginInfoApi, getUserListByRoleApi} from "@/api/user.js";

const router = useRouter()
const userStore = useUserStore()

// Query Form
const queryForm = reactive({
  hotelName:'',
  starRating:'',
  hotelAddress:'',
  userId:''
})

// Hotel User Data
const userList = ref([])
// Query Hotel User List
const getHotelUserList = async () => {
  const res = await getUserListByRoleApi(1,0)
  userList.value = res.data
}

// Hotel Data
const hotelList = ref([])
const hotelCurrentPage = ref(1) // Current Page
const hotelPageSize = ref(5) // Records per page
const hotelTotal = ref(10) // Total Records

// Get user data
const getHotelList = async (id, role) => {
  // If the user is not an admin, only query their own hotel data
  if (role && role !== 0) {
    queryForm.userId = id ? id : userStore.info.id;
  }
  const res = await getHotelPageApi(hotelCurrentPage.value, hotelPageSize.value, queryForm);
  hotelList.value = res.data.records;
  hotelTotal.value = res.data.total;
}

// Handle page number change
const handleHotelPageNumChange = (val) => {
  hotelCurrentPage.value = val;
  getHotelList();
}

// Handle page size change
const handleHotelPageSizeChange = (val) => {
  hotelPageSize.value = val;
  getHotelList();
}

// Control dialog visibility
const dialogVisible = ref(false);
// Indicator for whether the dialog is for editing or adding
const dialogFlag = ref(false);
// Edit form
const hotelForm = reactive({
  id: '',
  hotelName: '',
  hotelAddress: '',
  image: '',
  description: '',
  starRating: '',
  url: '',
  userId: '',
  state: '',
});
// Image upload
const fileList = ref([]);
// Upload success response
const handleUploadSuccess = (response, file, fileList) => {
  console.log(response, file, fileList);
  hotelForm.image = response.data;
  ElMessage.success('Image uploaded successfully');
};
// Upload error response
const handleUploadError = (err) => {
  console.error('Image upload failed', err);
  ElMessage.error('Image upload failed');
};
// Clear form content
const clearForm = () => {
  hotelForm.id = '';
  hotelForm.hotelName = '';
  hotelForm.hotelAddress = '';
  hotelForm.image = '';
  hotelForm.description = '';
  hotelForm.starRating = '';
  hotelForm.url = '';
  hotelForm.userId = '';
  hotelForm.state = '';
  fileList.value = [];
}

// Handle add response
const handleAdd = () => {
  dialogFlag.value = true;
  dialogVisible.value = true;
  clearForm();
  // If the user is not an admin, set userId to their own ID by default when adding
  if (userStore.info.role !== 0) {
    hotelForm.userId = userStore.info.id;
  }
}

// Handle update response
const handleUpdate = (row) => {
  dialogFlag.value = false;
  dialogVisible.value = true;
  clearForm();
  hotelForm.id = row.id;
  hotelForm.hotelName = row.hotelName;
  hotelForm.hotelAddress = row.hotelAddress;
  hotelForm.image = row.image;
  hotelForm.description = row.description;
  hotelForm.starRating = row.starRating;
  hotelForm.url = row.url;
  hotelForm.userId = row.userId;
  hotelForm.state = row.state;
  fileList.value = [];
  fileList.value.push({
    url: row.image,
  });
}

// Handle delete response
const handleDelete = (id) => {
  ElMessageBox.confirm(
      'Are you sure you want to delete this hotel data?',
      'Friendly Reminder',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    deleteHotelApi(id).then(res => {
      ElMessage({
        type: 'success',
        message: 'Deleted successfully',
      });
      getHotelList();
    });
  }).catch(() => {
    // Handle cancel
  });
}

// Handle room management response
const handleToRoomAdmin = (id) => {
  router.push("/room_admin/" + id);
}

// Form cancel response
const onCancel = () => {
  dialogVisible.value = false;
}

// Form submit response
const onSubmit = async () => {
  if (dialogFlag.value) {
    addHotelApi(hotelForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Added successfully',
      });
      hotelCurrentPage.value = Math.ceil((hotelTotal.value + 1) / hotelPageSize.value);
      getHotelList();
    });
  } else {
    updateHotelApi(hotelForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Updated successfully',
      });
      getHotelList();
    });
  }
  dialogVisible.value = false;
}

// Handle export response
const handleExport = () => {
  const token = localStorage.getItem('token');
  axios({
    url: '/api/hotel/export',
    method: 'GET',
    responseType: 'blob',  // Specify response data type as blob
    headers: {
      Authorization: `${token}`  // Add token to request headers
    }
  }).then(response => {
    const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = `Hotel_Data_${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}.xlsx`;
    link.click();
    // Success message for download
    ElMessage.success('Exported successfully');
  }).catch(error => {
    // Handle download failure
    ElMessage.error('Export failed');
  });
}

// Excel file
const excelFileList = ref([]);
// Import success response
const handleImportSuccess = (response, file, fileList) => {
  excelFileList.value = fileList;
  ElMessage.success('Excel file uploaded successfully');
  excelFileList.value = [];
  getHotelList();
}
const handleImportError = (err) => {
  ElMessage.error('Excel file upload failed');
  excelFileList.value = [];
}

onMounted(async () => {
  const res = await getLoginInfoApi();
  await getHotelList(res.data.id, res.data.role)
  await getHotelUserList()
})
</script>

<style scoped>

</style>