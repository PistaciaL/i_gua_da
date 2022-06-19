// pages/userManagement/userManagement.js
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
    users:[
      // {id:1,nickname:'刘智宇',studentNumber:'2019302654',status:'正常',permission:'管理员',email:'lha602@163.com'},
      // {id:2,nickname:'李畅',studentNumber:'2019302654',status:'封禁中',permission:'普通用户',email:'lha602@163.com'},
      // {id:3,nickname:'李畅',studentNumber:'2019302654',status:'封禁中',permission:'普通用户',email:'lha602@163.com'},
    ]
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
      this.reloadList();
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
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.reloadList()
  },
  reloadList(){
    app.globalData.axios({
      url: '/manager/searchUsers',
      method: 'POST',
      params:{
        info: this.data.searchInfo,
        page: this.data.currentPage,
        pageSize: 5,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        res.data.data.forEach((value,index,self)=>{
          value.permission = value.permission==1?'普通用户':'管理员';
          value.status = value.status==1?'正常':'封禁中';
        })
        this.setData({
          users: res.data.data,
          currentPage: res.data.page,
          totalPage: res.data.totalPageNumb
        })
      }
    })
  },
  setPermission(e){
    app.globalData.axios({
      url:'/manager/setPermission',
      method: 'POST',
      params:{
        userId: e.target.dataset.id,
        newPermission: e.target.dataset.newpermission,
        code: app.globalData.userCode
      }
    }).then(res=>{
      wx.showToast({
        title: '修改成功',
        icon: "none",
        duration: 3000
      })
      this.data.users.forEach((value,index,self)=>{
        if(value.userId==e.target.dataset.id){
          value.permission = e.target.dataset.newpermission==1?'普通用户':'管理员';
        }
      })
      this.setData({
        users:this.data.users
      })
    })
  },
  setStatus(e){
    app.globalData.axios({
      url:'/manager/setStatus',
      method: 'POST',
      params:{
        userId: e.target.dataset.id,
        newStatus: e.target.dataset.newstatus,
        code: app.globalData.userCode
      }
    }).then(res=>{
      wx.showToast({
        title: '修改成功',
        icon: "none",
        duration: 3000
      })
      this.data.users.forEach((value,index,self)=>{
        if(value.userId==e.target.dataset.id){
          value.status = e.target.dataset.newstatus==1?'正常':'封禁中';
        }
      })
      this.setData({
        users:this.data.users
      })
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