// pages/messageBoard/messageBoard.js
const app = getApp();
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
      if(this.data.input==''){
        wx.showToast({
          title: "输入不能为空!",
          icon: "none",
          duration: 1000
        });
      }else{
        app.globalData.axios({
          url:'/addMessage',
          method:'POST',
          params:{
            content:this.data.input,
            type:this.data.type,
            code:app.globalData.userCode
          }
        }).then(res=>{
          if(res.data.status==200){
            wx.showToast({
              title: "留言成功!",
              icon: "none",
              duration: 1000
            });
            setTimeout(()=>{
              wx.navigateBack({
                delta: 1,
              });
            },1000);
          }else{
            console.log(res.msg)
          }
        })
      }
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