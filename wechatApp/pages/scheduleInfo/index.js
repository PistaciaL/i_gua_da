// pages/scheduleInfo/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orders:[
      {scheduleId:1,departureDatetime:'2022/6/19 20:23',startCampusStr:'长安校区',startStation:'A站',endCampusStr:'友谊校区',endStation:'B站'}
    ],
    isManager:false,
    datetimePickershow: false,
    startStationPickerShow: false,
    endStationPickerShow: false,
    minHour: 10,
    maxHour: 20,
    minDate: new Date(2020, 1, 1).getTime(),
    maxDate: new Date(2029, 10, 1).getTime(),
    currentDate: new Date().getTime(),
    datetimeStr: '',
    startCampusStr: '长安校区',
    endCampusStr: '友谊校区',
    station:["长安校区","友谊校区"],
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
  yuyue(e){
    app.globalData.axios({
      url: '/user/reserve',
      method: 'POST',
      params: {
        scheduleId: e.target.dataset.id,
        code: app.globalData.userCode
      }
    }).then(res=>{
      if(res.data.status==200){
        wx.showToast({
          title: "预约成功",
          icon: "none",
          duration: 3000
        })
      }
    })
  },
  daohang(e){
    this.data.station.forEach((value,index,self)=>{
      if(value.id==e.target.dataset.id){
        app.globalData.notice = value;
      }
    })
    wx.redirectTo({
      url: '/pages/location/location',
    })
  },
  chaxun(){
    if(this.data.startCampusStr=='' || this.data.endCampusStr==''
      || this.data.datetimeStr==''){
      wx.showToast({
        title: "请输入查询条件",
        icon: "none",
        duration: 3000
      })
    }
    else if(this.data.startCampusStr==this.data.endCampusStr){
      wx.showToast({
        title: "起始站与终点站相同",
        icon: "none",
        duration: 3000
      })
    }else{
      app.globalData.axios({
        url: '/searchSchedule',
        method: 'POST',
        params: {
          startCampus: this.data.startCampusStr,
          endCampus: this.data.endCampusStr,
          departureDate: this.data.datetimeStr,
          page: 1,
          pageSize: 100,
          code: app.globalData.userCode
        }
      }).then(res=>{
        console.log(res.data.data)
        this.setData({
          orders: res.data.data
        })
      })
    }
  },
  /**
   * 日期选择器
   */
  onDisplayDatetimePicker() {
    this.setData({
        datetimePickershow: true
    })
  },
  onCloseDatetimePicker() {
    this.setData({
        datetimePickershow: false
    })
  },
  onConfirmDatetimeInput(event) {
    var formattedDatetime = this.formatDate(event.detail)
    this.setData({
        currentDate: event.detail,
        datetimeStr: formattedDatetime,
    });
    this.onCloseDatetimePicker();
  },
  /**
   * startStation选择器
   */
  onDisplayStartStationPicker() {
    this.setData({
      startStationPickerShow: true
    })
  },
  onCloseStartStationPicker() {
    this.setData({
      startStationPickerShow: false
    })
  },
  onConfirmStartStationInput(event) {
    this.setData({
      startCampusStr: event.detail.value,
    });
    this.onCloseStartStationPicker();
  },
  /**
   * endStation选择器
   */
  onDisplayEndStationPicker() {
    this.setData({
      endStationPickerShow: true
    })
  },
  onCloseEndStationPicker() {
    this.setData({
      endStationPickerShow: false
    })
  },
  onConfirmEndStationInput(event) {
    this.setData({
      endCampusStr: event.detail.value,
    });
    this.onCloseEndStationPicker();
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
   onInput(){

   },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let newDateStr = this.formatDate(this.data.currentDate)
    this.setData({ 
      isManager:app.globalData.isManager,
      datetimeStr: newDateStr
    })
    this.chaxun()
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