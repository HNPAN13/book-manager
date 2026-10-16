import request from '../utils/request'


// 根据id获取单本图书
export function getBookById(id) {
  return request({
    url: `/book/get/${id}`,
    method: 'get'
  })
}


// 登录接口
export const loginApi = (data) => {
  return request.post('/login', data)
}

// 图书分页列表
export const getBookList = (params) => {
  return request.get('/book/list', { params })
}

// 新增图书（后面用）
export const addBookApi = (data) => {
  return request.post('/book/add', data)
}

// 借阅记录列表
export const getBorrowRecordList = (params) => {
  return request.get('/borrow/list', { params })
}
