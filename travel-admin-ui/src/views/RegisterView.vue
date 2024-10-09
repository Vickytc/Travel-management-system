<template>
  <el-row style="padding-top: 20px;text-align: center">
    <el-col :span="6"></el-col>
    <el-col :span="12">
      <el-card shadow="always" style="width: 100%">
        <h1 style="text-align: center">User Registration</h1>
        <el-form :inline="true" ref="formRef" :rules="rules" label-width="auto"
                 :model="registerForm" style="width: 100%">
          <p>
            <el-form-item prop="username" label="Username" style="width: 45%">
              <el-input :prefix-icon="User" v-model.trim="registerForm.username" placeholder="Please enter your username"/>
            </el-form-item>
            <el-form-item prop="password" label="Password" style="width: 45%">
              <el-input type="password" :prefix-icon="Lock" v-model.trim="registerForm.password" placeholder="Please enter your password"/>
            </el-form-item>
          </p>
          <p>
            <el-form-item prop="email" label="Email" style="width: 45%">
              <el-input type="email" :prefix-icon="Message" v-model.trim="registerForm.email" placeholder="Please enter your registration email"/>
            </el-form-item>
            <el-form-item prop="contactNumber" label="Contact Number" style="width: 45%">
              <el-input :prefix-icon="Message" v-model.trim="registerForm.contactNumber" placeholder="Please enter your contact number"/>
            </el-form-item>
          </p>
          <p>
            <el-form-item prop="address" label="Address" style="width: 45%">
              <el-input :prefix-icon="Message" v-model.trim="registerForm.address" placeholder="Please enter your address"/>
            </el-form-item>
            <el-form-item prop="idCard" label="ID Card" style="width: 45%">
              <el-input :prefix-icon="Message" v-model.trim="registerForm.idCard" placeholder="Please enter your ID card"/>
            </el-form-item>
          </p>
          <p>
            <el-form-item prop="businessRegistrationNumber" label="Business Number" style="width: 45%">
              <el-input :prefix-icon="Message" v-model.trim="registerForm.businessRegistrationNumber" placeholder="Please enter your business registration number"/>
            </el-form-item>
            <el-form-item prop="role" label="Registration Role" style="width: 45%">
              <el-radio-group v-model="registerForm.role">
                <el-radio :value="2">Customer</el-radio>
                <el-radio :value="1">Merchant</el-radio>
              </el-radio-group>
            </el-form-item>
          </p>
          <p>
            <el-form-item prop="verificationCode" label="Verification Code" style="width: 45%">
              <el-input v-model="registerForm.code" maxlength="6" placeholder="Email verification code">
                <template #append>
                  <input type="button" :plain="true" @click="getCode()" :disabled="state.show"
                         style="width: 100%;height: 100%;border: 1px;background: none;width: 50px;color: #ababab;"
                         :value="state.codeText" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="age" label="Age" style="width: 45%">
              <el-input-number v-model="registerForm.age" :min="1" :max="120" size="small"/>
            </el-form-item>
          </p>
          <el-form-item>
            <div class="g-recaptcha" data-sitekey="6Ld3n1gqAAAAADxnVsTNUIChvKeL-Su7yF6IPuQe"></div>
          </el-form-item>
          <el-form-item>
            <!--            <el-button type="default" @click="handleGoogleLogin">Register with Google account</el-button>-->
          </el-form-item>

          <el-form-item style="width: 100%">
            <el-button type="primary" @click="onSubmit(formRef)" style="width: 100%">Register</el-button>
          </el-form-item><br/>
          <el-form-item>
            <el-link @click="goToLogin" style="float: left;">Already have an account? Go to login</el-link>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <el-col :span="6"></el-col>
  </el-row>
</template>


<script setup>
import {ref, reactive, onMounted } from 'vue'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import {Lock, User, Message, Right} from '@element-plus/icons-vue'
import {sendRegisterCodeApi, userRegisterApi} from "@/api/user.js";

const router = useRouter();

// Registration validation rules
const formRef = ref()
// Verification code non-empty and correctness validation
const validateVerificationCode = (rule, value, callback) => {
  // Check if the email regex passes; if not, skip this field validation
  formRef.value.validateField('email', err => {
    if (err) {
      if (!value) {
        return callback(new Error('Please fill in this field~'))
      } else {
        return callback()
      }
    } else {
      return callback(new Error('Please fill in the email to get the verification code~'))
    }
  })
}
const rules = {
  username: [
    { required: true, message: 'Please enter your username', trigger: 'blur' },
    { min: 2, max: 16, message: 'Username length must be between 2 and 16 characters', trigger: 'blur' },
  ],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' },
    { min: 3, max: 14, message: 'Password length must be between 3 and 14 characters', trigger: 'blur' },
  ],
  email: [
    { required: true, message: 'Please enter your email', trigger: 'blur' },
    {
      pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
      message: 'Please enter a valid email address',
      trigger: 'blur'
    },
  ],
  contactNumber: [
    { required: true, message: 'Please enter your contact number', trigger: 'blur' },
  ],
  address: [
    { required: true, message: 'Please enter your address', trigger: 'blur' },
  ],
  businessRegistrationNumber: [
    { required: true, message: 'Please enter your business registration number', trigger: 'blur' },
  ],
  idCard: [
    { required: true, message: 'Please enter your ID card', trigger: 'blur' },
  ],
  age: [
    { required: true, message: 'Please enter your age', trigger: 'blur' },
  ],
  role: [
    { required: true, message: 'Please select a registration role', trigger: 'blur' },
  ],
  code: [{
    required: true,
    validator: validateVerificationCode,
    trigger: 'blur'
  }],
}

// Registration form data
const registerForm = reactive({
  username: '',
  password: '',
  age: '',
  email: '',
  contactNumber: '',
  address: '',
  businessRegistrationNumber: '',
  idCard: '',
  role: '',
  code: '',
  recaptchaResponse: ''
})

// Registration submission
const onSubmit = async (formEl) => {
  try {
    if (formEl) {
      formEl.validate(async (valid, fields) => {
        if (valid) {
          console.log(grecaptcha.getResponse())
          registerForm.recaptchaResponse = grecaptcha.getResponse();
          userRegisterApi(registerForm).then(res => {
            ElMessage({
              message: "Registration successful, redirecting to login page",
              type: 'success',
            })
            // Add a one-second timer
            setTimeout(() => {
              router.push("/login");
            }, 1000);
          })
        } else {
          ElMessage.error('Please fill in the form correctly')
        }
      })
    }
  } catch (err) {
    ElMessage.error("Registration failed, please try again later!");
    console.error(err);
  }
}

// Page state
const state = reactive({
  // Get verification code, disable after clicking
  show: false,
  // Get verification code text, change after clicking
  codeText: 'Get',
  // Current seconds
  count: '',
  // Timer
  timer: null,
})

// 60 seconds countdown for verification code
const TIME_COUNT = 60;

// Notify backend to send verification code
const getCode = async () => {
  if (state.show) {
    return; // If counting, return directly
  }

  // Check if the email regex passes
  const emailValid = await new Promise((resolve) => {
    formRef.value.validateField('email', (err) => {
      resolve(!err); // Return validation result
    });
  });

  if (emailValid) {
    ElMessage.error('Please fill in a valid email');
    return;
  }

  // If valid, notify backend to send verification code
  state.show = true; // Start timing
  const res = await sendRegisterCodeApi(registerForm.email);

  if (res.code === 200) {
    ElMessage.success("Verification code has been sent to the email");
    // Start the timer after the backend successfully sends
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
        state.codeText = "Resend";
      }
    }, 1000);
  } else {
    ElMessage.error("Verification code sending failed, please try again");
    state.show = false; // Allow resending if sending failed
  }
};

// Handle Google registration button response
const handleGoogleLogin = async () => {
  window.clientId = '665952417972-7j14chjhvo3614ege4mis88se7vatcum.apps.googleusercontent.com'; // Replace with your client ID
  window.redirectUri = 'http://localhost:9999/user/register/google'; // Replace with your redirect URI
  window.scope = 'email profile';
  window.state = 'random_state_string'; // Generate random string
  window.responseType = 'code';

  window.authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${window.clientId}&redirect_uri=${window.redirectUri}&scope=${window.scope}&state=${window.state}&response_type=${window.responseType}`;

  // Redirect to Google authorization page
  window.location.href = window.authUrl;
};

// Jump to the login page
const goToLogin = () => {
  router.push("/login");
};

// Load reCAPTCHA script after the page is mounted
onMounted(() => {
  const script = document.createElement('script');
  script.src = 'https://www.google.com/recaptcha/api.js';
  script.async = true;
  script.defer = true;
  document.body.appendChild(script);
});

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
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
