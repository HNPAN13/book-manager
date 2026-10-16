<template>
  <div class="page">
    <div class="page-header">
      <h2>图书管理</h2>
      <button class="logout-btn" @click="handleLogout">退出登录</button>
    </div>

    <div class="card">
      <div class="search-area">
        <input v-model="searchKey" placeholder="输入书名/作者查询" />
        <button class="btn btn-primary" @click="getBookList()">查询</button>
        <button class="btn btn-warning" @click="searchKey='';getBookList()">重置</button>
        <button class="btn btn-success" @click="openAdd">新增图书</button>
      </div>

      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>书名</th>
            <th>作者</th>
            <th>库存</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in bookList" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.bookName }}</td>
            <td>{{ item.author }}</td>
            <td>{{ item.stock }}</td>
            <td>
              <div class="operation-group">
                <button class="btn btn-primary" @click="openEdit(item)">编辑</button>
                <button class="btn btn-danger" @click="delBook(item.id)">删除</button>
                <button class="btn btn-success" @click="openBorrowDialog(item.id)" :disabled="item.stock <= 0">借书</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <span>共 {{ total }} 条</span>
        <button class="btn btn-primary" @click="pageNum--;getBookList()" :disabled="pageNum <=1">上一页</button>
        <span>第 {{ pageNum }} 页</span>
        <button class="btn btn-primary" @click="pageNum++;getBookList()" :disabled="pageNum * pageSize >= total">下一页</button>
      </div>
    </div>

    <!-- 新增/编辑弹窗 遮罩层 -->
    <div v-if="dialogShow" class="dialog-box">
      <div class="dialog-content">
        <h4>{{form.id ? '编辑图书' : '新增图书'}}</h4>
        <div class="form-item">
          <label>书名：</label>
          <input v-model="form.bookName"/>
        </div>
        <div class="form-item">
          <label>作者：</label>
          <input v-model="form.author"/>
        </div>
        <div class="form-item">
          <label>库存：</label>
          <input v-model.number="form.stock"/>
        </div>
        <div class="dialog-footer">
          <button class="btn btn-primary" @click="submit">提交</button>
          <button class="btn btn-warning" @click="dialogShow=false">取消</button>
        </div>
      </div>
    </div>

    <!-- 借书弹窗 遮罩层 -->
    <div v-if="borrowDialogShow" class="dialog-box">
      <div class="dialog-content">
        <h4>确认借阅图书</h4>
        <div class="form-item">图书名称：{{ borrowInfo.bookName }}</div>
        <div class="form-item">作者：{{ borrowInfo.author }}</div>
        <div class="form-item">当前库存：{{ borrowInfo.stock }}</div>
        <div class="dialog-footer">
          <button class="btn btn-primary" @click="submitBorrow">确认借书</button>
          <button class="btn btn-warning" @click="borrowDialogShow=false">取消</button>
        </div>
      </div>
    </div>

    <div class="back-link">
      <button class="btn btn-primary" @click="$router.push('/borrowRecord')">前往借阅记录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { useRouter } from 'vue-router'
const router = useRouter()
const bookList = ref([])
const dialogShow = ref(false)
// =========分页变量=========
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
// ✅搜索关键词
const searchKey = ref("")
const form = ref({
  id:null,
  bookName:'',
  author:'',
  stock:0
})
// 借书弹窗变量
const borrowDialogShow = ref(false)
const borrowInfo = ref({})
// 获取图书列表：带上分页+搜索参数
const getBookList = async () => {
  try {
    const res = await request.get('/book/list',{
      params:{
        pageNum: pageNum.value,
        pageSize: pageSize.value,
         key: searchKey.value
      }
    })
    if(res.data.code === 200){
      bookList.value = res.data.data.records
      total.value = res.data.data.total
    }
  } catch(err) {
    console.error("加载图书列表失败：", err)
  }
}
//打开借书弹窗
const openBorrowDialog = async (bookId) => {
  try {
    const res = await request.get(`/book/get/${bookId}`)
    if(res.data.code === 200){
      borrowInfo.value = res.data.data
      borrowDialogShow.value = true
    }
  } catch (err) {
    alert("获取图书信息失败")
    console.error(err)
  }
}
//打开新增
const openAdd = ()=>{
  form.value = {id:null,bookName:'',author:'',stock:0}
  dialogShow.value = true
}
//打开编辑
const openEdit = (row)=>{
  form.value = {...row}
  dialogShow.value = true
}
//提交新增/编辑
const submit = async ()=>{
  try{
    if(form.value.id){
      await request.put('/book/update',form.value)
    }else{
      await request.post('/book/add',form.value)
    }
    dialogShow.value = false
    pageNum.value = 1
    await getBookList()
  }catch(err){
    console.error("提交失败",err)
    alert("操作异常")
  }
}
//删除
const delBook = async (id)=>{
  console.log("点击删除，id是：", id)
  if(confirm("确定删除？")){
    try{
      await request.delete(`/book/delete/${id}?operatorRole=admin`)
      pageNum.value = 1
      getBookList()
      alert("删除成功！")
    }catch(err){
      console.log("删除报错",err)
      alert("删除失败")
    }
  }
}
//提交借书
const submitBorrow = async ()=>{
  try{
    const userId = localStorage.getItem("userId")
    if(!userId){
      alert("请先登录！")
      borrowDialogShow.value = false
      return
    }
    const bookId = borrowInfo.value.id
    const res = await request.post('/book/borrowBook',null,{
      params:{
        bookId: bookId,
        userId: userId
      }
    })
    if(res.data.code === 200){
      borrowDialogShow.value = false
      pageNum.value = 1
      getBookList()
      alert("借书成功！")
    }else{
      alert(res.data.msg)
    }
  }catch(err){
    alert("操作失败："+ (err.response?.data?.msg || err.message))
  }
}
// 退出登录
const handleLogout = async ()=>{
  try{
    await request.post('/logout')
  }catch(e){
    console.log("退出接口异常")
  }finally{
    localStorage.removeItem("hasLogin")
    localStorage.removeItem("userId")
    localStorage.removeItem("operatorRole")
    router.push('/login')
  }
}
onMounted(()=>{
  getBookList()
})
</script>

<style scoped>
.page {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 16px;
}

.page-header h2 {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.logout-btn {
  background: #ffffff;
  color: #409eff;
  border: 1px solid #409eff;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: #409eff;
  color: #fff;
}

.card {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.search-area {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  align-items: center;
}

.search-area input {
  padding: 9px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  width: 240px;
}

.search-area input:focus {
  border-color: #409eff;
  outline: none;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.25s;
}

.btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

.btn-primary {
  background-color: #409eff;
  color: #ffffff;
}
.btn-primary:hover {
  background-color: #66b1ff;
}

.btn-success {
  background-color: #67c23a;
  color: #fff;
}
.btn-success:hover {
  background-color: #85ce61;
}

.btn-warning {
  background-color: #e6a23c;
  color: #fff;
}
.btn-warning:hover {
  background-color: #ebb563;
}

.btn-danger {
  background-color: #f56c6c;
  color: #fff;
}
.btn-danger:hover {
  background-color: #f78989;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  background-color: #409eff;
  color: #ffffff;
  padding: 12px;
  text-align: center;
  font-weight: normal;
}

.table td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #ebeef5;
}

.table tbody tr:hover {
  background-color: #f5f7fa;
}

.operation-group {
  display: flex;
  gap: 6px;
  justify-content: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  gap:12px;
  align-items: center;
  justify-content: flex-end;
  font-size: 14px;
  color:#606266;
}

/* 弹窗遮罩 */
.dialog-box {
  position: fixed;
  top: 0;
  left: 0;
  width:100%;
  height:100%;
  background-color: rgba(0,0,0,0.45);
  display:flex;
  justify-content:center;
  align-items:center;
  z-index:999;
}

.dialog-content {
  background:#fff;
  border-radius:8px;
  padding:24px;
  width:420px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.dialog-content h4 {
  margin:0 0 20px 0;
  font-size:17px;
  color:#303133;
}

.form-item {
  margin-bottom:14px;
}

.form-item input {
  width:100%;
  margin-top:4px;
  padding:9px 12px;
  border:1px solid #dcdfe6;
  border-radius:4px;
}

.dialog-footer {
  margin-top:24px;
  display:flex;
  justify-content:flex-end;
  gap:10px;
}

.back-link {
  margin-top:18px;
}
</style>
