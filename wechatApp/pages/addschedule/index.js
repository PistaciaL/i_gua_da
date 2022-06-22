// pages/addSchedule/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isManager:app.globalData.isManager,
    datetimePickershow: false,
    startStationPickerShow: false,
    endStationPickerShow: false,
    minHour: 10,
    maxHour: 20,
    minDate: new Date(2020, 1, 1).getTime(),
    maxDate: new Date(2029, 10, 1).getTime(),
    currentDate: new Date().getTime(),
    datetimeStr: '',
    startStationStr: '',
    startCampus: '',
    startStationId: null,
    endStationStr: '',
    endCampus: '',
    endStationId: null,
    station:[
        {stationId: 1,campus: "长安校区",station: "大飞机站",},
        {stationId: 3,campus: "长安校区",station: "启真楼站",},
        {stationId: 5,campus: "长安校区",station: "和尊站",},
        {stationId: 2,campus: "友谊校区",station: "老图书馆站",},
        {stationId: 4,campus: "友谊校区",station: "南门站",},
        {stationId: 6,campus: "友谊校区",station: "公字楼站",}
    ],
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
        if (type === 'hour') {
            return `${value}时`;
        }
        if (type === 'minute') {
            return `${value}分`;
        }
        return value;
    },
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
      startStationId: event.detail.value.stationId,
      startStationStr: event.detail.value.station,
      startCampus: event.detail.value.campus
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
      endStationId: event.detail.value.stationId,
      endStationStr: event.detail.value.station,
      endCampus: event.detail.value.campus
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
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    return y + '/' + m + '/' + d + ' ' + h + ':' + minute;
   },
   
   /**
    * 提交表单
    */
   addSchedule(){
     if(this.data.startStationId==null||this.data.endStationId==null|| 
        this.data.datetimeStr==''){
      wx.showToast({
        title: '参数为空',
        icon: "none",
        duration: 3000
      })
     }
     else if(this.data.startCampus==this.data.endCampus){
      wx.showToast({
        title: '起始站与终点站在同一校区',
        icon: "none",
        duration: 3000
      })
     }
     else{
       app.globalData.axios({
         url:'/manager/addSchedule',
         method: 'POST',
         params: {
           departureDatetime: this.data.datetimeStr,
           startStationId: this.data.startStationId,
           endStationId: this.data.endStationId,
           code: app.globalData.userCode
         }
       }).then(res=>{
        if(res.data.status==200){
          wx.showToast({
            title: '添加成功',
            icon: "none",
            duration: 3000
          })
          let nowDate = new Date().getTime()
          this.setData({
            currentDate: nowDate,
            datetimeStr: '',
            startStationId: null,
            endStationId: null,
            startCampus:'',
            endCampus: '',
            startStationStr: '',
            endStationStr: ''
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