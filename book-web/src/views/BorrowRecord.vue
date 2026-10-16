<template>
  <div class="page">
    <div class="page-header">
      <h2>借阅记录</h2>
      <button class="btn btn-primary" @click="$router.push('/book-manage')">返回图书管理</button>
    </div>

    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>借阅ID</th>
            <th>图书名称</th>
            <th>借阅人</th>
            <th>借阅时间</th>
            <th>归还时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in borrowList" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.bookName }}</td>
            <td>{{ item.username }}</td>
            <td>{{formatDateTime(item.borrowTime)}}</td>
            <td>{{formatDateTime(item.returnTime)}}</td>

            <td>
              <span class="status-tag" :class="{noReturn: item.isReturn===0}">
                {{item.isReturn===0 ? '未归还':'已归还'}}
              </span>
            </td>
            <td>
              <button v-if="item.isReturn === 0" class="btn btn-success" @click="returnBook(item.id)" :disabled="submitting">
                还书
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button class="btn btn-primary" @click="pageNum--" :disabled="pageNum <=1">上一页</button>
        <span>第 {{pageNum}} 页 / 共 {{totalPage}} 页</span>
        <button class="btn btn-primary" @click="pageNum++" :disabled="pageNum >= totalPage">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import request from '@/utils/request'
const pageNum = ref(1)
const pageSize = ref(10)
const borrowList = ref([])
const submitting = ref(false)
const totalPage = ref(0)

// ✅ 放到顶层，所有函数外面，模板才可以调用
const formatDateTime = (timeStr) =>{
  if(!timeStr) return '-'
  return timeStr.replace('T',' ')
}

const loadBorrow = async () => {
  const res = await request.get("/borrow/list", {
    params: {
      current: pageNum.value,
      size: pageSize.value
    }
  })
  console.log("完整返回res：", res)
  // 你的是 data.data 层级
  if(res.data.code === 200){
    borrowList.value = res.data.data.records
    totalPage.value = res.data.data.pages
  }
}
watch(pageNum, ()=>{
  loadBorrow()
})

const returnBook = async (borrowId) => {
  if (!confirm("确认还书？")) return
  submitting.value = true
  try {
    const res = await request.post('/borrow/returnBook',null,{
      params:{
        recordId: borrowId
      }
    })
    if (res.data.code === 200) {
      loadBorrow()
    } else {
      alert(res.data.msg || '还书失败')
    }
  } catch (err) {
    console.error('还书异常', err)
    alert('网络异常，还书失败')
  } finally {
    submitting.value = false
  }
}

onMounted(loadBorrow)
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

.card {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size:14px;
  cursor: pointer;
  transition: all 0.25s;
}

.btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

.btn-primary {
  background-color: #409eff;
  color: #fff;
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

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  background-color: #409eff;
  color: #ffffff;
  padding:12px;
  text-align:center;
  font-weight:normal;
}

.table td {
  padding:12px;
  text-align:center;
  border-bottom:1px solid #ebeef5;
}

.table tbody tr:hover {
  background-color: #f5f7fa;
}

.status-tag {
  display: inline-block;
  padding:3px 10px;
  border-radius:12px;
  font-size:13px;
  background:#909399;
  color:#fff;
}
.status-tag.noReturn {
  background:#67c23a;
}

.pagination {
  margin-top:20px;
  display:flex;
  gap:12px;
  align-items:center;
  justify-content:flex-end;
  font-size:14px;
  color:#606266;
}
</style>
