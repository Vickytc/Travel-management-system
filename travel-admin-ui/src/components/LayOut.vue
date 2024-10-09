<template>
  <div class="common-layout">
    <el-container>
      <!-- Left menu -->
      <el-aside width="200px">
        <el-menu :default-active="activeIndex" class="el-menu-demo"
                 active-text-color="#E6A23C" background-color="rgb(48, 65, 86)" text-color="#fff" router>
          <el-menu-item>
            <h3 style="color: #409EFF">Travel Admin System</h3>
          </el-menu-item>
          <!-- Customer -->
          <el-menu-item v-if="userStore.info.role === 2" index="/hotel">
            <el-icon><HomeFilled /></el-icon>
            <span>Home</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 2" index="/reserve_admin">
            <el-icon><Grid /></el-icon>
            <span>My Reserve</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 2" index="/comment_admin">
            <el-icon><List /></el-icon>
            <span>My Comment</span>
          </el-menu-item>

          <!-- Merchant -->
          <el-menu-item v-if="userStore.info.role === 1" index="/hotel_admin">
            <el-icon><Tools /></el-icon>
            <span>Hotel Admin</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 1" index="/reserve_admin">
            <el-icon><List /></el-icon>
            <span>Reserve Admin</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 1" index="/comment_admin">
            <el-icon><Comment /></el-icon>
            <span>Comment Admin</span>
          </el-menu-item>

          <!-- Administrator -->
          <el-menu-item v-if="userStore.info.role === 0" index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>Dashboard</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 0" index="/user_admin">
            <el-icon><User /></el-icon>
            <span>User Admin</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 0" index="/hotel_admin">
            <el-icon><Tools /></el-icon>
            <span>Hotel Admin</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 0" index="/reserve_admin">
            <el-icon><List /></el-icon>
            <span>Reserve Admin</span>
          </el-menu-item>
          <el-menu-item v-if="userStore.info.role === 0" index="/comment_admin">
            <el-icon><Comment /></el-icon>
            <span>Comment Admin</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <!-- Top header -->
        <el-header style="text-align: right; font-size: 12px">
          <div class="toolbar">
            <span style="margin-right: 8px"><el-avatar :size="45" :src="userStore.info.image" /></span>
            <span style="margin-right: 5px;color: white">{{ userStore.info.username}}</span>
            <el-button type="danger" size="small" @click="logout">Logout</el-button>
          </div>
        </el-header>

        <!-- Management page -->
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from "@/stores/info.js";
import { useRouter } from "vue-router";
import { getLoginInfoApi } from "@/api/user.js";

const userStore = useUserStore()
const router = useRouter()

const activeIndex = ref('')

// Get login information
const getLoginInfo = async () => {
  let result = await getLoginInfoApi();
  if (result.data) {
    userStore.setInfo(result.data)
  } else {
    router.push('/login')
  }
}

// Logout button response
const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(async () => {
  await getLoginInfo()
  if (userStore.info.role === 0) {
    activeIndex.value = '/user_admin'
  } else if (userStore.info.role === 1) {
    activeIndex.value = '/hotel_admin'
  } else if (userStore.info.role === 2) {
    activeIndex.value = '/hotel'
  }
})
</script>

<style lang="scss" scoped>
.el-container {
  height: 100vh;
  background-color: #eee;
  .el-aside {
    background-color: rgb(48, 65, 86);
    .el-menu {
      border-right: none;
    }
  }
  .el-header {
    position: relative;
    background-color: #303133;
    color: var(--el-text-color-primary);
  }
  .el-main, .el-footer {
    background-color: #fff;
    margin: 10px 0;
  }
  .el-footer {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}
.common-layout {
  margin-left: -8px;
  margin-right: -8px;
  margin-top: -8px;
}
.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>
