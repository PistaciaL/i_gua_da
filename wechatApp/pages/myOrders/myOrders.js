// pages/myOrders/myOrders.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageLock:true,
    isManager:app.globalData.isManager,
    currentPage:1,
    totalPage:8,
    orders:[
      // {id:1,startCampus:'长安校区',startStation:'A站',endCampus:'友谊校区',endStation:'C站',startTime:'2022/6/17 12:00',status:'正常'},
      // {id:2,startCampus:'长安校区',startStation:'A站',endCampus:'友谊校区',endStation:'C站',startTime:'2022/6/17 12:00',status:'正常'},
      // {id:3,startCampus:'长安校区',startStation:'A站',endCampus:'友谊校区',endStation:'C站',startTime:'2022/6/17 12:00',status:'正常'},
    ]
  },
  goto(e){
    var url = e.target.dataset.url
    wx.redirectTo({
      url: url,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadList()
  },
  cancelReserve(e){
    app.globalData.axios({
      url: "/user/cancelReserve",
      method: 'POST',
      params: {
        reserveId: e.target.dataset.id,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        this.loadList()
      }
      wx.showToast({
        title: '取消预约成功',
        icon: "none",
        duration: 3000
      })
    })
  },
  loadList(){
    app.globalData.axios({
      url: '/user/futureReserve',
      method: 'POST',
      params:{
        page: this.data.currentPage,
        pageSize: 5,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        this.setData({
          currentPage: res.data.page,
          totalPage: res.data.totalPageNumb,
          orders: res.data.data
        })
      }
      else {
        this.setData({
          currentPage: 0,
          totalPage: 0,
          orders: []
        });
        wx.showToast({
          title: res.data.msg,
          icon: "none",
          duration: 3000
        })
      }
    })
  },
  prevPage(){
    if(1<this.data.currentPage&&this.data.currentPage<=this.data.totalPage&&this.data.pageLock){
      this.data.currentPage--;
      this.data.pageLock = false;
      setTimeout(()=>{
        this.data.pageLock = true;
      },500)
      this.loadList();
      //console.log(this.data.currentPage);
    }else if(this.data.pageLock==false){
      wx.showToast({
        title: '请勿点击过快!',
        icon: "none",
        duration: 400
      })
    }
  },
  nextPage(){
    if(1<=this.data.currentPage&&this.data.currentPage<this.data.totalPage&&this.data.pageLock){
      this.data.currentPage++;
      this.data.pageLock = false;
      setTimeout(()=>{
        this.data.pageLock = true;
      },500)
      this.loadList();
      //console.log(this.data.currentPage);
    }else if(this.data.pageLock==false){
      wx.showToast({
        title: '请勿点击过快!',
        icon: "none",
        duration: 400
      })
    }
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