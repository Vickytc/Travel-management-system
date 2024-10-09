<template>
  <el-card>User Management</el-card>

  <el-card style="margin-top: 10px">
    <!-- Query form -->
    <el-form :inline="true" :model="queryForm" class="form-inline">
      <el-form-item label="Username">
        <el-input v-model.trim="queryForm.username" placeholder="Please enter username" clearable />
      </el-form-item>
      <el-form-item label="Role">
        <el-select style="width: 200px" v-model="queryForm.role"
                   placeholder="Please select role" clearable>
          <el-option label="Admin" :value="0" />
          <el-option label="Merchant" :value="1" />
          <el-option label="Customer" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getUserList">Query</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="handleAdd">Add</el-button>
        </el-form-item>
      </el-form-item>
    </el-form>

    <!-- Display table -->
    <el-table :data="userList" style="width: 100%">
      <el-table-column label="Username" prop="username">
      </el-table-column>
      <el-table-column label="Avatar">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-avatar :size="45" :src="scope.row.image" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Email" prop="email">
      </el-table-column>
      <el-table-column label="Role">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="primary" v-if="scope.row.role === 0">Admin</el-tag>
            <el-tag type="warning" v-else-if="scope.row.role === 1">Merchant</el-tag>
            <el-tag type="success" v-else-if="scope.row.role === 2">Customer</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Status">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="primary" v-if="scope.row.state === 0">Active</el-tag>
            <el-tag type="danger" v-else-if="scope.row.state === 1">Banned</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Registration Time" prop="createTime">
      </el-table-column>

      <el-table-column label="Actions">
        <template #default="scope">
          <el-button type="warning" size="small" @click="handleUpdate(scope.row)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-block">
      <el-pagination
          v-model:current-page="userCurrentPage"
          v-model:page-size="userPageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="userTotal"
          @size-change="handleUserPageSizeChange"
          @current-change="handleUserPageNumChange"
      />
    </div>
  </el-card>

  <!-- Add/Update dialog -->
  <el-dialog v-model="dialogVisible" :title="dialogFlag ? 'Add User' : 'Edit User'" width="700">
    <el-form :model="userForm" label-width="auto">
      <el-form-item label="Username">
        <el-input v-model="userForm.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Password" v-if="dialogFlag">
        <el-input v-model="userForm.password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="userForm.email" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="contactNumber" label="Contact Number">
        <el-input v-model.trim="userForm.contactNumber" placeholder="Please enter contact number"/>
      </el-form-item>
      <el-form-item prop="address" label="Address">
        <el-input v-model.trim="userForm.address" placeholder="Please enter address"/>
      </el-form-item>
      <el-form-item prop="idCard" label="ID Card">
        <el-input v-model.trim="userForm.idCard" placeholder="Please enter ID card"/>
      </el-form-item>
      <el-form-item label="Business Number">
        <el-input v-model.trim="userForm.businessRegistrationNumber" placeholder="Please enter business registration number"/>
      </el-form-item>
      <el-form-item label="Age">
        <el-input-number v-model="userForm.age" :min="1" :max="120" />
      </el-form-item>
      <el-form-item label="Role">
        <el-radio-group v-model="userForm.role">
          <el-radio :value="2">Customer</el-radio>
          <el-radio :value="1">Merchant</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Avatar">
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
      <el-form-item label="Status" v-if="!dialogFlag">
        <el-select v-model="userForm.state" placeholder="Select user status" style="width: 240px">
          <el-option :key="0" label="Active" :value="0"/>
          <el-option :key="1" label="Banned" :value="1"/>
        </el-select>
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
import {addUserApi, deleteUserApi, getUserPageApi, updateUserApi} from "@/api/user.js";
import {ElMessage, ElMessageBox} from "element-plus";

// Control dialog visibility
const dialogVisible = ref(false)
// Identify whether the dialog is for editing or adding
const dialogFlag = ref(false)
// Query form data
const queryForm = reactive({
  username: '',
  role: '',
})
// Form for editing or adding
const userForm = reactive({
  id: '',
  username: '',
  password: '',
  address: '',
  age: '',
  image: '',
  businessRegistrationNumber: '',
  contactNumber: '',
  email: '',
  idCard: '',
  state: '',
  role: ''
})
// Clear form content
const clearForm = () => {
  userForm.id = ''
  userForm.username = ''
  userForm.password = ''
  userForm.address = '',
      userForm.age = '',
      userForm.image = '',
      userForm.businessRegistrationNumber = '',
      userForm.contactNumber = '',
      userForm.email = '',
      userForm.idCard = '',
      userForm.state = ''
  userForm.role = ''
  fileList.value = []
}
// Image upload
const fileList = ref([]);
// Upload success response
const handleUploadSuccess = (response, file, fileList) => {
  console.log(response, file, fileList)
  userForm.image = response.data
  ElMessage.success('Image uploaded successfully');
};
// Upload failure response
const handleUploadError = (err) => {
  console.error('Image upload failed', err);
  ElMessage.error('Image upload failed');
};


// User data
const userList = ref([])
const userCurrentPage = ref(1) // Current page number
const userPageSize = ref(5) // Number of records displayed per page
const userTotal = ref(10) // Total number of records
// Get user data
const getUserList = async () => {
  const res = await getUserPageApi(userCurrentPage.value, userPageSize.value, queryForm)
  userList.value = res.data.records;
  userTotal.value = res.data.total;
}
// Handle page number change
const handleUserPageNumChange = (val) => {
  userCurrentPage.value = val
  getUserList();
}
// Handle page size change
const handleUserPageSizeChange = (val) => {
  userPageSize.value = val
  getUserList();
}

// Handle add button response
const handleAdd = () => {
  dialogFlag.value = true
  dialogVisible.value = true
  clearForm()
}
// Handle edit button response
const handleUpdate = (row) => {
  dialogFlag.value = false
  dialogVisible.value = true
  clearForm()
  userForm.id = row.id
  userForm.username = row.username
  userForm.password = row.password
  userForm.address = row.address
  userForm.age = row.age
  userForm.image = row.image
  userForm.businessRegistrationNumber = row.businessRegistrationNumber
  userForm.contactNumber = row.contactNumber
  userForm.email = row.email
  userForm.idCard = row.idCard
  userForm.state = row.state
  userForm.role = row.role
  fileList.value = []; // Clear fileList
  fileList.value.push({
    url: row.image, // URL of the image
  });
}
// Handle delete button response
const handleDelete = (id) => {
  ElMessageBox.confirm(
      'Are you sure you want to delete this user?',
      'Friendly Reminder',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    deleteUserApi(id).then(res => {
      ElMessage({
        type: 'success',
        message: 'Deleted successfully',
      })
      getUserList();
    })
  }).catch(() => {

  })
}
// Form cancel response
const onCancel = () => {
  dialogVisible.value = false
}
// Form submit response
const onSubmit = async () => {
  if (dialogFlag.value) {
    addUserApi(userForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Added successfully',
      })
      userCurrentPage.value = Math.ceil((userTotal.value + 1) / userPageSize.value);
      getUserList();
    })
  } else {
    updateUserApi(userForm).then(res => {
      ElMessage({
        type: 'success',
        message: 'Updated successfully',
      })
      getUserList();
    })
  }
  dialogVisible.value = false
}

onMounted(() => {
  getUserList()
})
</script>

<style scoped>

</style>