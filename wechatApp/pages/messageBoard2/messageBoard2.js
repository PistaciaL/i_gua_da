// pages/messageBoard2/messageBoard2.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    type:1,
    title:['失物寻主','寻物启事','乘车意见','其他..'],
    messages:[
      // {messageId:1,content:'10号车失物寻主手表',type:1},
      // {messageId:2,content:'10号车失物寻主手表',type:1},
      // {messageId:3,content:'10号车失物寻主手表',type:1},
      // {messageId:4,content:'10号车失物寻主手表',type:1},
    ]
  },
  choose(e){
    this.data.type = e.target.dataset.type;
    this.getMessages();
  },
  getMessages(){
    app.globalData.axios({
      url:'/manager/getMessageByType',
      method:'POST',
      params:{
        type:this.data.type,
        code:app.globalData.userCode
      }
    }).then(res=>{
      console.log(res.data);
      if(res.status==200){
        // console.log(res.data);
        this.setData({
          messages:res.data.data,
          type:this.data.type
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getMessages();
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