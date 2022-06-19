// pages/myCenter/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isManager:false,
    userName:''
  },
  goto(e){
    var url = e.target.dataset.url
    if(url!='/pages/scheduleInfo/index'){
      wx.navigateTo({
        url: url,
      })
    }else{
      wx.redirectTo({
        url: url,
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      userName: app.globalData.userName,
      isManager:app.globalData.isManager
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