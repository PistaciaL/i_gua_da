// pages/location/location.js
var QQMapWX = require('../../libs/qqmap-wx-jssdk');
var qqmapsdk;
let _this;
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    desLocation:{
      latitude: 0,
      longitude: 0
    },
    nowLocation:{
      latitude: 0,
      longitude: 0 
    },
    latitude: 0,
    longitude: 0,
    polyline: null,
    plusResult: {
      distance: 0,
      duration: 0
    },
    stationInfo: [
      {name: '大飞机站', info:'西北工业大学（长安校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://www.jt720.cn/pano/f4bf97cf4b047e05?s=scene_34535b9212fcf492'},
      {name: '老图书馆站', info:'西北工业大学（友谊校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://720yun.com/t/85eye7l26oue9gajh4?pano_id=9d8qDEINIw4F7Myy'},
      {name: '启真楼站', info:'西北工业大学（长安校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://www.jt720.cn/pano/f4bf97cf4b047e05?s=scene_b8829905abf6c680'},
      {name: '南门站', info:'西北工业大学（友谊校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://720yun.com/t/85eye7l26oue9gajh4?pano_id=VOFWYlbCwQuyfrTA'},
      {name: '和尊站', info:'西北工业大学（长安校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://www.jt720.cn/pano/f4bf97cf4b047e05?s=scene_47ee8ca7c275f28c'},
      {name: '公字楼站', info:'西北工业大学（友谊校区）', imgSrc:'http://101.35.0.204:8080/creeper/videos/nwpu.png', url:'https://720yun.com/t/85eye7l26oue9gajh4?pano_id=tcXeReQq4nc1wCHH'},
    ],
    station:{
      name: '', 
      info:'', 
      imgSrc:'', 
      url:''
    },
    updateLocationTime : new Date().getTime()
  },
  gotoVR(){
    app.globalData.vrPageURL=this.data.station.url
    wx.navigateTo({
      url: '/pages/vrModel/index'
    })
  },
  updateLocation(){
    wx.onLocationChange(res=>{
      console.log('location change', res)
      _this.setData({
        nowLocation:{
          latitude: res.latitude,
          longitude: res.longitude
        }
      })
      var nowTime=new Date().getTime()
      if(nowTime-_this.data.updateLocationTime>5000){
        _this.refreshMap()
        _this.setData({
          updateLocationTime: nowTime
        })
      }
    })
  },
  refreshMap(){
    console.log('start refresh')
    // 调用接口
    qqmapsdk.direction({
      mode: 'walking',
      from: _this.data.nowLocation,
      to: _this.data.desLocation,
      success: function(res){
        var ret = res;
        var coors = ret.result.routes[0].polyline, pl = [];
        //坐标解压（返回的点串坐标，通过前向差分进行压缩）
        var kr = 1000000;
        for (var i = 2; i < coors.length; i++) {
          coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
        }
        //将解压后的坐标放入点串数组pl中
        for (var i = 0; i < coors.length; i += 2) {
          pl.push({ latitude: coors[i], longitude: coors[i + 1] })
        }
        //设置polyline属性，将路线显示出来,将解压坐标第一个数据作为起点
        if(_this.data.latitude==0 || _this.data.longitude==0){
          _this.setData({
            latitude:pl[0].latitude,
            longitude:pl[0].longitude,
          })
        }
        _this.setData({
          plusResult: {
            distance: ret.result.routes[0].distance,
            duration: ret.result.routes[0].duration
          },
          polyline: [{
            points: pl,
            color: '#7CAAFF',
            width: 4
          }]
        })
      },
      complete: function(res){
        console.log('complete refresh:',res)
      }
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('options',options)
    this.setData({
      desLocation:{
        latitude: options.latitude,
        longitude: options.longitude,
      }
    })
    this.data.stationInfo.forEach((value,index,self)=>{
      if(value.name==options.station){
        this.setData({
          station: value
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
  onShow: function () {
    _this = this;
    // 实例化API核心类
    qqmapsdk = new QQMapWX({
        key: 'CXRBZ-LJKKK-BSGJF-AX2HH-4C45J-X6BD4'
    });
    //开始获取位置
    wx.startLocationUpdate({
      success: (res) => {
        console.log(res)
      },
    });
    //首次获取当前位置
    wx.getLocation({
      type: 'wgs84',
      success (res) {
        _this.setData({
          nowLocation:{
            latitude: res.latitude,
            longitude: res.longitude
          }
        })
        _this.refreshMap()
      }
    })
    this.updateLocation()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    wx.stopLocationUpdate({
      success: (res) => {
        console.log(res)
      },
    })
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    wx.stopLocationUpdate({
      success: (res) => {
        console.log(res)
      },
    })
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