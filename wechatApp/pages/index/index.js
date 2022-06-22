// pages/index/index.js
const app = getApp();
const axios = app.globalData.axios;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    date:'',
    show:false,
    isManager:false,
    notices: null,
    schedules: null,
  },
  goto2(e){
    wx.redirectTo({
      url: e.currentTarget.dataset.url,
    })
  },
  gotoNotice(e){
    this.data.notices.forEach((value,index,self)=>{
      if(value.noticeId==e.target.dataset.id){
        app.globalData.notice = value;
      }
    })
    wx.navigateTo({
      url: '/pages/noticeDetail/index',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({ 
      isManager:app.globalData.isManager
    })
    app.globalData.axios({
      url: "/getNotices",
      method: "POST",
      params: {
        page: 1,
        pageSize: 5
      }
    }).then(res=>{
      this.setData({
        notices: res.data.data
      })
    })
    app.globalData.axios({
      url: "/getTodaySchedule",
      method: "POST",
      params: {
        page: 1,
        pageSize: 20
      }
    }).then(res=>{
      // console.log(res.data);
      if(res.data.status==200){
        this.setData({
          schedules: res.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})