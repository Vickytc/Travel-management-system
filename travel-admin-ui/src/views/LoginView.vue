<template>
  <el-row style="padding-top: 220px;">
    <el-col :span="9"></el-col>
    <el-col :span="6">
      <el-card style="max-width: 500px" shadow="always">
        <h1 style="text-align: center">Tour Management System</h1>
        <el-form ref="formRef" :rules="rules" label-width="auto"
                 :model="loginForm" style="max-width: 600px">
          <el-form-item prop="username" :rules="rules.username">
            <el-input :prefix-icon="User" v-model.trim="loginForm.username" placeholder="Please enter your login username"/>
          </el-form-item>
          <el-form-item prop="password" :rules="rules.password">
            <el-input type="password" :prefix-icon="Lock" v-model.trim="loginForm.password" placeholder="Please enter your login password"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit(formRef)" style="width: 100%">Login</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-bottom: 60px">
          <el-link @click="goToRegister" style="float: left">Don't have an account? Go to register</el-link>
        </div>
      </el-card>
    </el-col>
    <el-col :span="9"></el-col>
  </el-row>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { Lock, User } from '@element-plus/icons-vue';
import { getLoginInfoApi, userLoginApi } from "@/api/user.js";

const router = useRouter();

// Login validation rules
const formRef = ref();
const rules = {
  username: [
    { required: true, message: 'Please enter your username', trigger: 'blur' },
    { min: 2, max: 16, message: 'Username length must be between 2 and 16 characters', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' },
    { min: 3, max: 14, message: 'Password length must be between 3 and 14 characters', trigger: 'blur' },
  ]
};

// Login form data
const loginForm = reactive({
  username: '',
  password: '',
});

// Login submission
const onSubmit = async (formEl) => {
  if (formEl) {
    formEl.validate(async (valid, fields) => {
      if (valid) {
        await userLoginApi(loginForm).then(async res => {
          ElMessage({
            message: "Login successful",
            type: 'success',
          })
          localStorage.setItem("token", res.data)
          let result = await getLoginInfoApi();
          if (result.data.role === 0) {
            router.push('/user_admin')
          } else if (result.data.role === 1) {
            router.push('/hotel_admin')
          } else if (result.data.role === 2) {
            router.push('/hotel')
          }
        })
      }
    });
  }
};

// Navigate to the registration page
const goToRegister = () => {
  router.push("/register");
};
</script>

<style scoped lang="scss">
body, html {
  height: 100%;
  margin: 0;
}

.el-row {
  background-image: url("../../public/login.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
  height: 100vh;
}
</style>
