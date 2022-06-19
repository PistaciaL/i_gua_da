import axios from "/utils/axios"
import mpAdapter from '/utils/axios-miniprogram-adapter.js'
axios.defaults.adapter = mpAdapter
axios.defaults.baseURL='http://101.35.0.204:8081'
// axios.defaults.timeout = 6000
// app.js
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        let resCode = res.code
        axios({
          url:'/login',
          method:'POST',
          params:{
            code:res.code
          }
        }).then(res=>{
          if(res.data.status==430){
            this.globalData.userCode = resCode;
            wx.redirectTo({
              url: '/pages/register/index',
            })
          }else if(res.data.status==200){
            this.globalData.userCode = resCode;
            this.globalData.userName = res.data.name;
            this.globalData.shcoolId = res.data.studentNumber;
            this.globalData.isManager = (res.data.permission==1?false:true);
            wx.redirectTo({
              url: '/pages/index/index',
            })
          }
        })
      }
    })

  },
  globalData: {
    userName: null,
    shcoolId:null,
    userCode: null,
    isManager:true,
    axios:axios,
    notice:{
      id: null,
      title: '',
      content: '',
      sender: '',
      createTime: ''
    }
  },
  getGlobalData() {
    return this.globalData
  }
})
