// pages/noticeManagement/noticeManagement.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageLock:true,
    isManager:app.globalData.isManager,
    searchInfo:'',
    currentPage:1,
    totalPage:8,
    notices:[]
  },
  goto(e){
    var url = e.target.dataset.url
    wx.navigateTo({
      url: url,
    })
  },
  updateInput(event){
    this.setData({
      searchInfo:event.detail.value
    })
  },
  search(){
    this.reloadList()
  },
  prevPage(){
    if(1<this.data.currentPage&&this.data.currentPage<=this.data.totalPage&&this.data.pageLock){
      this.data.currentPage--;
      this.data.pageLock = false;
      setTimeout(()=>{
        this.data.pageLock = true;
      },500)
      this.reloadList();
      // console.log(this.data.currentPage);
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
      this.reloadList();
      // console.log(this.data.currentPage);
    }else if(this.data.pageLock==false){
      wx.showToast({
        title: '请勿点击过快!',
        icon: "none",
        duration: 400
      })
    }
  },
  processNotices(){
    this.data.notices.forEach((value,index,self)=>{
      if(value.content.length>60){
        value.show = value.content.slice(0,57)+'...';
      }else{
        value.show = value.content;
      }
    });
    this.setData({
      notices:this.data.notices
    })
  },
  reloadList(){
    app.globalData.axios({
      url:'/manager/searchNotices',
      method: 'POST',
      params:{
        info: this.data.searchInfo,
        page: this.data.currentPage,
        pageSize: 5,
        code: app.globalData.userCode
      }
    }).then(res=>{
      console.log(res.data)
      if(res.data.status==200){
        this.setData({
          notices: res.data.data,
          currentPage: res.data.page,
          totalPage: res.data.totalPageNumb
        })
        this.processNotices();
      }
    })
  },
  deleteNotice(e){
    console.log(e.target.dataset.id)
    app.globalData.axios({
      url:'/manager/deleteNotice',
      method:'POST',
      params:{
        noticeId: e.target.dataset.id,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        wx.showToast({
          title: '删除成功',
          icon: "none",
          duration: 3000
        })
        this.reloadList()
      }
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
    this.reloadList();
    // console.log(this.data.notices);
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