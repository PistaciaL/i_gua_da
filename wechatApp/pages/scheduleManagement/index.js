// pages/scheduleManagement/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageLock:true,
    startDatePickerShow: false,
    endDatePickerShow: false,
    startDateStr: '',
    endDateStr: '',
    startDate: new Date().getTime(),
    endDate: new Date().getTime(),
    minDate: new Date(2020, 1, 1).getTime(),
    maxDate: new Date(2029, 10, 1).getTime(),
    isManager:app.globalData.isManager,
    currentPage:1,
    totalPage:8,
    schedules:[],
    formatter(type, value) {
      if (type === 'year') {
          return `${value}年`;
      }
      if (type === 'month') {
          return `${value}月`;
      }
      if (type === 'day') {
          return `${value}日`;
      }
      return value;
    },
  },
  /*开始时间选择器 */
  onStartDatePickerShow(){
    this.setData({
      startDatePickerShow : true
    })
  },
  onCloseStartDatePicker(){
    this.setData({
      startDatePickerShow: false
    })
  },
  onConfirmStartDate(event) {
    var formattedDatetime = this.formatDate(event.detail)
    this.setData({
        startDate: event.detail,
        startDateStr: formattedDatetime,
    });
    this.onCloseStartDatePicker();
  },
  /*结束时间选择器 */
  onEndDatePickerShow(){
    this.setData({
      endDatePickerShow : true
    })
  },
  onCloseEndDatePicker(){
    this.setData({
      endDatePickerShow: false
    })
  },
  onConfirmEndDate(event) {
    var formattedDatetime = this.formatDate(event.detail)
    this.setData({
        endDate: event.detail,
        endDateStr: formattedDatetime,
    });
    this.onCloseEndDatePicker();
  },
  /**
   * 格式化datetime
   */
  formatDate(inputTime) {
    var date = new Date(inputTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '/' + m + '/' + d;
  },
  // 跳转
  goto(e){
    var url = e.target.dataset.url
    wx.navigateTo({
      url: url,
    })
    // wx.redirectTo({
    //   url: url,
    // })
  },
  search(){
    this.reloadList()
  },
  onInput(){

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.reloadList()
  },
  prevPage(){
    if(1<this.data.currentPage&&this.data.currentPage<=this.data.totalPage&&this.data.pageLock){
      this.data.currentPage--;
      this.data.pageLock = false;
      setTimeout(()=>{
        this.data.pageLock = true;
      },500)
      //this.reloadList();
      console.log(this.data.currentPage);
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
      //this.reloadList();
      console.log(this.data.currentPage);
    }else if(this.data.pageLock==false){
      wx.showToast({
        title: '请勿点击过快!',
        icon: "none",
        duration: 400
      })
    }
  },
  reloadList(){
    app.globalData.axios({
      url:'/manager/searchSchedules',
      method:'POST',
      params:{
        startDate: this.data.startDateStr,
        endDate: this.data.endDateStr,
        page: this.data.currentPage,
        pageSize: 5,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        this.setData({
          schedules: res.data.data,
          currentPage: res.data.page,
          totalPage: res.data.totalPageNumb
        })
      }
      else {
        this.setData({
          schedules: []
        })
        wx.showToast({
          title: res.data.msg,
          icon: "none",
          duration: 3000
        })
      }
    })
  },
  deleteSchedule(e){
    console.log(e.target.dataset.id)
    app.globalData.axios({
      url:'/manager/deleteSchedule',
      method: 'POST',
      params:{
        scheduleId: e.target.dataset.id,
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
      else{
        wx.showToast({
          title: res.data.msg,
          icon: "none",
          duration: 3000
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