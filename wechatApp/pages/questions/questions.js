// pages/questions/questions.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    submitLock:true,
    currentQuestion:{},
    questions:[
      // {id:1,title:'垃圾分类的精髓是？',A:'答案请选择正确的答案请选择正确的答案',B:'请选择正确的答案请选择正确的答案请选择正确的答案',C:'请选择正确',D:'请选择正确的答案请选择正确的答案请',answer:'B'},
      // {id:2,title:'Li勇你他妈是傻逼吧？',A:'是是是',B:'啊对对对',C:'是你妈',D:'傻软',answer:'B'},
      // {id:3,title:'sdfsdfs？',A:'sdgsg',B:'sdgsd',C:'sdgsd',D:'tyugkgyu',answer:'B'},
      // {id:4,title:'dfbfdxb？',A:'售电公司',B:'s的方式告诉',C:'发给他',D:'彻底删除',answer:'B'},
      // {id:5,title:'速度非常？',A:'打发是',B:'一哭二天',C:'疯牛病',D:'电饭煲',answer:'B'},
      // {id:6,title:'等服务？',A:'s是否v',B:'发到网上',C:'豆腐干汇报',D:'的风格好办',answer:'B'},
      // {id:7,title:'从v吧v？',A:'等方式',B:'幸福吧',C:'第三次',D:'不那么',answer:'B'},
      // {id:8,title:'现场水电费？',A:'速度非常',B:'啊打发沙发沙发',C:'目的上大学',D:'从v现在v',answer:'B'},
      // {id:9,title:'手电狗？',A:'s水电费',B:'二亿人',C:'地方干部',D:'提供的发挥',answer:'B'},
      // {id:10,title:'自行车我的身份证？',A:'都无法发送',B:'编不下去了',C:'瞎JB打字',D:'wocap',answer:'B'},
    ],
    index:1,
    choice:'',
    answers:'',
    correctNumber:0
  },
  choiceAnswer(e){
    this.setData({
      choice:e.target.dataset.choice
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    app.globalData.axios({
      url:'/getQuestion',
      method:'POST',
      params:{
        code:app.globalData.userCode
      }
    }).then(res=>{
      // console.log(res);
      if(res.data.status==200){
        this.setData({
          questions:res.data.data,
          currentQuestion:res.data.data[0]
        })
      }
    })
  },
  next(){
    if(this.data.choice==''){
      wx.showToast({
        title: "请选择答案!",
        icon: "none",
        duration: 1000
      });
    }else{
      this.data.answers+=this.data.choice;
      if(this.data.choice==this.data.questions[this.data.index-1].answer){
        this.data.correctNumber++;
        wx.showToast({
          title: "回答正确!",
          icon: "none",
          duration: 1000
        });
      }else{
        wx.showToast({
          title: "回答错误!正确答案为"+this.data.questions[this.data.index-1].answer,
          icon: "none",
          duration: 1000
        });
      }
      this.data.index++;
      this.setData({
        index:this.data.index,
        choice:'',
        currentQuestion:this.data.questions[this.data.index-1]
      })
    }
  },
  submit(){
    if(this.data.choice==''){
      wx.showToast({
        title: "请选择答案!",
        icon: "none",
        duration: 1000
      });
    }else if(this.data.submitLock){
      this.data.submitLock = false;
      this.data.answers+=this.data.choice;
      if(this.data.choice==this.data.questions[this.data.index-1].answer){
        this.data.correctNumber++;
        wx.showToast({
          title: "回答正确!您共答对"+this.data.correctNumber+'题!',
          icon: "none",
          duration: 1000
        });
      }else{
        wx.showToast({
          title: "回答错误!正确答案为"+this.data.questions[this.data.index-1].answer+'!您共答对'+this.data.correctNumber+'题!',
          icon: "none",
          duration: 1000
        });
      }
      // console.log(this.data.answers);

      setTimeout(()=>{
        wx.navigateBack({
          delta: 1,
        });
      },1000);
    }
  },
  back(){
    wx.navigateBack({
      delta: 1,
    });
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