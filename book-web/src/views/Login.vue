<template>
  <div class="login-box">
    <el-card class="login-card">
      <h2 class="login-title">图书管理系统登录</h2>
      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
      <p class="register-tip">没有账号？<a @click="$router.push('/register')">立即注册</a></p>

    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi } from '../api/book'
const router = useRouter()
//新增
const goRegister = () => {
  router.push('/register')
}
const loginForm = ref({
  username: '',
  password: ''
})
const handleLogin = async () => {
  console.log('点击登录，表单数据：', loginForm.value)
  try {
    const res = await loginApi(loginForm.value)
    console.log('后端返回res：', res)
    console.log('res.data.code =', res.data.code)
    if(res.data.code === 200){
      // 保存登录状态 + 用户ID
      localStorage.setItem("hasLogin", "1")
      localStorage.setItem("userId", res.data.data.id)
      // 可选：保存用户角色，后面编辑图书权限校验用
      localStorage.setItem("operatorRole", res.data.data.role)
      router.push('/book-manage')
      console.log("登录成功，跳转主页，userId已保存")
    } else {
      alert(res.data.msg || '登录失败')
    }
  } catch (err) {
    console.error('❌ 全部异常捕获：', err)
    alert('出错：' + err.message)
  }
}
</script>

<style scoped>
.login-box {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 420px;
  border-radius: 8px;
}

.login-title {
  text-align: center;
  margin: 10px 0 24px 0;
  color: #303133;
  font-size: 22px;
}

.login-btn {
  width: 100%;
}

.register-tip {
  margin-top:8px;
  text-align:center;
  font-size:14px;
  color:#606266;
}

.register-tip a {
  color:#409eff;
  cursor:pointer;
}
.register-tip a:hover {
  text-decoration: underline;
}
.link-text {
  color:#409eff;
  cursor:pointer;
}
.link-text:hover {
  text-decoration: underline;
}

</style>
