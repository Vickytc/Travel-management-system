<template>
  <el-card>Comment Management</el-card>

  <el-card style="margin-top: 10px">
    <!-- Query Form -->
    <el-form :inline="true" :model="queryForm" class="form-inline">
      <el-form-item label="Comment Status">
        <el-select style="width: 200px" v-model="queryForm.state"
                   placeholder="Please select comment status" clearable>
          <el-option label="Unreviewed" :value="0" />
          <el-option label="Reviewed" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getCommentList(userStore.info.id, userStore.info.role)">Query</el-button>
        </el-form-item>
      </el-form-item>
    </el-form>

    <!-- Display Table -->
    <el-table :data="commentList" style="width: 100%">
      <el-table-column label="Commenter" prop="username">
      </el-table-column>
      <el-table-column label="Avatar" width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-avatar :size="45" :src="scope.row.image" />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Comment Content" prop="content">
      </el-table-column>
      <el-table-column label="Service Rating" prop="serviceRating">
        <template #default="scope">
          <el-rate v-model="scope.row.serviceRating" disabled/>
        </template>
      </el-table-column>
      <el-table-column label="Status">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-tag type="warning" v-if="scope.row.state === 0">Unreviewed</el-tag>
            <el-tag type="primary" v-else-if="scope.row.state === 1">Reviewed</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="Creation Time" prop="createTime">
      </el-table-column>

      <el-table-column label="Action" width="220">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">Delete</el-button>
          <el-button type="warning" size="small" @click="handleUpdate(scope.row, 1)"
                     v-if="scope.row.state === 0 && userStore.info.role !== 2">Approve</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-block">
      <el-pagination
          v-model:current-page="commentCurrentPage"
          v-model:page-size="commentPageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="commentTotal"
          @size-change="handleCommentPageSizeChange"
          @current-change="handleCommentPageNumChange"
      />
    </div>
  </el-card>

</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import {deleteCommentApi, getCommentAllByUserIdApi, getCommentPageApi, updateCommentApi} from "@/api/comment.js";
import { useUserStore } from "@/stores/info.js";
import {getLoginInfoApi} from "@/api/user.js";

const router = useRouter();
const userStore = useUserStore();

// Query Form
const queryForm = reactive({
  userId: '',
  state: '',
  hotelId: '',
});

// Comment Data
const commentList = ref([]);
const commentCurrentPage = ref(1); // Page number
const commentPageSize = ref(5); // Records displayed per page
const commentTotal = ref(10); // Total records
// Get Comment Data
const getCommentList = async (id, role) => {
  commentList.value = []
  // If the user is not an admin, only query their comments
  if (role === 1) {
    const result = await getCommentAllByUserIdApi(id ? id : userStore.info.id, commentCurrentPage.value, commentPageSize.value)
    commentList.value = result.data.records
    commentTotal.value = result.data.total;
    return;
  } else if (role === 2) {
    queryForm.userId = userStore.info.id;
  }
  const res = await getCommentPageApi(commentCurrentPage.value, commentPageSize.value, queryForm);
  commentList.value = res.data.records;
  commentTotal.value = res.data.total;
};
// Handle page number change
const handleCommentPageNumChange = (val) => {
  commentCurrentPage.value = val;
  getCommentList(userStore.info.id, userStore.info.role);
};
// Handle page size change
const handleCommentPageSizeChange = (val) => {
  commentPageSize.value = val;
  getCommentList(userStore.info.id, userStore.info.role);
}

// Edit Form
const commentForm = reactive({
  id: '',
  content: '',
  serviceRating: '',
  hotelId: '',
  roomId: '',
  userId: '',
  state: ''
});
// Clear Form Content
const clearForm = () => {
  commentForm.id = '';
  commentForm.content = '';
  commentForm.serviceRating = '';
  commentForm.hotelId = '';
  commentForm.roomId = '';
  commentForm.userId = '';
  commentForm.state = '';
};
// Handle Comment Review Status Response
const handleUpdate = async (row, state) => {
  clearForm();
  commentForm.id = row.id;
  commentForm.content = row.content;
  commentForm.serviceRating = row.serviceRating;
  commentForm.hotelId = row.hotelId;
  commentForm.roomId = row.roomId;
  commentForm.userId = row.userId;
  commentForm.state = state;
  await updateCommentApi(commentForm).then(res => {
    ElMessage({
      type: 'success',
      message: 'Modification successful',
    });
    getCommentList(userStore.info.id, userStore.info.role);
  });
};
// Handle Delete Response
const handleDelete = (id) => {
  ElMessageBox.confirm(
      'Are you sure you want to delete this comment?',
      'Friendly Reminder',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    deleteCommentApi(id).then(res => {
      ElMessage({
        type: 'success',
        message: 'Deletion successful',
      });
      getCommentList(userStore.info.id, userStore.info.role);
    });
  }).catch(() => {
  });
}

onMounted(async () => {
  const res = await getLoginInfoApi();
  await getCommentList(res.data.id, res.data.role);
});
</script>

<style scoped>

</style>
