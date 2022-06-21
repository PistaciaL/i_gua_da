// pages/messageBoard/messageBoard.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    type:0,
    input:''
  },
  updateInput(e){
    this.setData({
      input:e.detail.value
    })
  },
  choose(e){
    this.setData({
      type:e.target.dataset.type
    })
  },
  submit(){
    if(this.data.type==0){
      wx.showToast({
        title: "请选择留言类型!",
        icon: "none",
        duration: 1000
      });
    }else{
      console.log(this.data.input);
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