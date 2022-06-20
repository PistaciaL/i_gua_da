// pages/register/index.js
const app = getApp();
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var timer;
let that;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    nickName: '',
    studentNumber: '',
    email: '',
    verificationCode: '',
    sendCodeMsgDefault: '发送验证码',
    sendCodeMsg: '发送验证码',
    sendCodetime: 0,
    sendCodeIsAble: true
  },
  onInputNickname(e){
    this.setData({
      nickName: e.detail
    })
  },
  onInputStudentNumber(e){
    this.setData({
      studentNumber: e.detail
    })
  },
  onInputEmail(e){
    this.setData({
      email: e.detail
    })
  },
  onInputVerificationCode(e){
    this.setData({
      verificationCode: e.detail
    })
  },
  sendCode(){
    var nowTime = new Date().getTime()
    if(nowTime-this.data.sendCodetime > 60*1000) {
      //执行发送邮件逻辑代码
      app.globalData.axios({
        url: "/sendEmail",
        method: "POST",
        params: {
          code: app.globalData.userCode,
          email: this.data.email
        }
      }).then(res=>{
        console.log(res.data)
        if(res.data.status==200){
          this.setData({
            sendCodeIsAble: false,
            sendCodetime: nowTime,
            sendCodeMsg: 60
          });
          //启动定时任务
          count()
        }
        else {
          wx.showToast({
            title: "发送失败",
            icon: "none",
            duration: 3000
          })
        }
      })
      
    }
  },
  submitUserMsg(){
    // console.log(this.data)
    console.log(app.globalData)
    app.globalData.axios({
      url:'/register',
      method:'POST',
      params:{
        nickname:this.data.nickName,
        studentNumber:this.data.studentNumber,
        email:this.data.email,
        verificationCode:this.data.verificationCode,
        code:app.globalData.userCode,
      }
    }).then(res=>{
      console.log(res.data);
      if(res.data.status==200){
        app.globalData.userName = res.data.name;
        app.globalData.shcoolId = res.data.studentNumber;
        app.globalData.isManager = (res.data.permission==1?false:true)
        wx.showToast({
          title: "信息绑定成功!",
          icon: "none",
          duration: 3000
        })
        wx.redirectTo({
          url: '/pages/index/index',
        })
      }else{
        console.log(res.data.msg)
        wx.showToast({
          title: res.data.msg,
          icon: "none",
          duration: 3000
        })
      }
    })
  },
  onLoad(){
    that = this;
  }
});
function count(){
  timer = setTimeout(function () {
    let sendCodeMsg = that.data.sendCodeMsg
    let sendCodeMsgDefault = that.data.sendCodeMsgDefault
    if(sendCodeMsg>0){
      that.setData({
        sendCodeMsg: sendCodeMsg-1
      })
    } else {
      that.setData({
        sendCodeIsAble: true,
        sendCodeMsg: sendCodeMsgDefault
      })
    }
    count();
    if(sendCodeMsg == sendCodeMsgDefault) {
      clearTimeout(timer)
    }
  }, 1000);
}