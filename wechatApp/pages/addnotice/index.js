// pages/addnotice/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    noticeTitle: '',
    noticeContent: ''
  },
  onInputNoticeTitle(e){
    this.setData({
      noticeTitle: e.detail
    })
  },
  onInputNoticeContent(e){
    this.setData({
      noticeContent: e.detail
    })
  },
  addNotice(){
    if(this.data.noticeTitle==''||this.data.noticeContent==''){
      wx.showToast({
        title: '输入不能为空！',
        icon: "none",
        duration: 3000
      })
    }else {
      app.globalData.axios({
        url: '/manager/addNotice',
        method: 'POST',
        params:{
          title: this.data.noticeTitle,
          content: this.data.noticeContent,
          code: app.globalData.userCode
        }
      }).then(res=>{
        if(res.data.status==200){
          wx.showToast({
            title: '添加成功',
            icon: "none",
            duration: 3000
          })
          setTimeout(()=>{
            wx.redirectTo({
              url: '/pages/noticeManagement/noticeManagement',
            })}, 1000)
        }
        else {
          wx.showToast({
            title: '添加失败',
            icon: "none",
            duration: 3000
          })
        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

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