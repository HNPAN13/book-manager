<template>
  <div class="register-box">
    <div class="register-card">
      <h2 class="register-title">用户注册</h2>
      <div class="form-item">
        <label>账号</label>
        <input v-model="form.username" class="input-box" placeholder="请输入账号"/>
      </div>
      <div class="form-item">
        <label>密码</label>
        <input v-model="form.password" type="password" class="input-box" placeholder="请输入密码"/>
      </div>
      <div class="form-item">
        <label>确认密码</label>
        <input v-model="form.rePwd" type="password" class="input-box" placeholder="再次输入密码"/>
      </div>
      <button @click="handleRegister" class="register-btn">注册</button>
      <p class="tip-text">已有账号？<span class="link-text" @click="goLogin">去登录</span></p>
    </div>
  </div>
</template>
<script setup>
import {ref} from 'vue'
import request from '@/utils/request'
import {useRouter} from 'vue-router'
const router = useRouter()

// 新增跳转登录方法
const goLogin = ()=>{
  router.push('/login')
}

const form = ref({
  username:'',
  password:'',
  rePwd:''
})
const handleRegister = async ()=>{
  if(!form.value.username || !form.value.password){
    alert("账号密码不能为空")
    return
  }
  if(form.value.password !== form.value.rePwd){
    alert("两次密码不一致")
    return
  }
  const res = await request.post("/user/register",form.value)
  if(res.data.code === 200){
    alert("注册成功，请登录")
    router.push("/login")
  }else{
    alert(res.data.msg)
  }
}
</script>
<style scoped>
.register-box {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}
.register-card {
  width: 420px;
  background: #ffffff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.register-title {
  text-align: center;
  margin: 0 0 24px 0;
  color: #303133;
  font-size: 22px;
}
.form-item {
  margin-bottom: 16px;
}
.form-item label {
  display: block;
  margin-bottom: 6px;
  font-size:14px;
  color:#606266;
}
.input-box {
  width:100%;
  box-sizing: border-box;
  padding: 10px 12px;
  border:1px solid #dcdfe6;
  border-radius:4px;
  font-size:14px;
}
.input-box:focus {
  outline: none;
  border-color:#409eff;
}
.register-btn {
  width:100%;
  padding:10px;
  background-color:#409eff;
  color:#fff;
  border: none;
  border-radius:4px;
  font-size:15px;
  cursor:pointer;
  transition: 0.25s;
}
.register-btn:hover {
  background-color:#66b1ff;
}
.tip-text {
  text-align:center;
  margin-top:16px;
  font-size:14px;
  color:#606266;
}
.link-text {
  color:#409eff;
  cursor:pointer;
}
.link-text:hover {
  text-decoration: underline;
}
</style>
