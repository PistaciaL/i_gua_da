"use strict";(self["webpackChunkwork"]=self["webpackChunkwork"]||[]).push([[615],{9615:function(t,s,e){e.r(s),e.d(s,{default:function(){return c}});var i=function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",{staticClass:"back"},[i("div",{staticClass:"nav"},[i("img",{attrs:{src:e(5080)}}),i("div",{on:{click:t.logout}},[i("span",[t._v("退出登录")])]),i("div",{on:{click:function(s){return t.goto("/manager/busManage")}}},[i("span",[t._v("管理员入口")])]),i("div",{on:{click:function(s){return t.goto("/user/userInfo")}}},[i("span",[t._v("个人信息")])]),i("div",{on:{click:function(s){return t.goto("/user/busTime")}}},[i("span",[t._v("预约校车")])])]),i("div",{staticClass:"content clearfix"},[i("div",{staticClass:"left-content"},[i("el-menu",{staticClass:"menu",attrs:{collapse:t.isCollapse,router:!0}},[i("el-menu-item",{on:{click:function(s){t.isCollapse=!t.isCollapse}}},[0==t.isCollapse?i("i",{staticClass:"el-icon-arrow-left"}):t._e(),1==t.isCollapse?i("i",{staticClass:"el-icon-arrow-right"}):t._e()]),i("el-submenu",{attrs:{index:"1"}},[i("template",{slot:"title"},[i("i",{staticClass:"el-icon-user-solid"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("个人信息")])]),i("el-menu-item",{attrs:{index:"/user/userInfo"}},[i("i",{staticClass:"el-icon-document"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("修改信息")])]),i("el-menu-item",{attrs:{index:"/user/password"}},[i("i",{staticClass:"el-icon-lock"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("修改密码")])])],2),i("el-submenu",{attrs:{index:"2"}},[i("template",{slot:"title"},[i("i",{staticClass:"el-icon-truck"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("校车信息")])]),i("el-menu-item",{attrs:{index:"/user/order"}},[i("i",{staticClass:"el-icon-star-on"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("我的预约")])]),i("el-menu-item",{attrs:{index:"/user/busTime"}},[i("i",{staticClass:"el-icon-s-data"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("校车班次")])])],2),i("el-submenu",{attrs:{index:"3"}},[i("template",{slot:"title"},[i("i",{staticClass:"el-icon-message-solid"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("意见反馈")])]),i("el-menu-item",{attrs:{index:"#",disabled:""}},[i("i",{staticClass:"el-icon-s-promotion"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("留言板")])]),i("el-menu-item",{attrs:{index:"#",disabled:""}},[i("i",{staticClass:"el-icon-phone-outline"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("联系管理员")])])],2),i("el-menu-item",{attrs:{index:"/manager/busManage"}},[i("i",{staticClass:"el-icon-s-platform"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("管理员入口")])]),i("el-menu-item",{attrs:{index:"/"}},[i("i",{staticClass:"el-icon-refresh-left"}),i("span",{attrs:{slot:"title"},slot:"title"},[t._v("返回首页")])])],1)],1),i("div",{staticClass:"right-content"},[i("router-view")],1)]),i("div",{staticClass:"bottom"},[i("div",{staticClass:"bottom-logo"},[i("img",{staticClass:"logo2",attrs:{src:e(1823)}})]),i("div",{staticClass:"line"}),t._m(0),i("div",{staticClass:"bottom-logo2"},[i("img",{staticClass:"logo3",attrs:{src:e(237)}})])])])},a=[function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"bottom-info"},[e("p",[t._v("i瓜大开发团队,联系电话:18322805616")]),e("p",[t._v("团队成员:刘智宇,康弘,卜欣阳,董保朔,冶小东")]),e("p",[t._v("地址:西北工业大学长安校区")]),e("p",[t._v("版权所有 © i瓜大开发团队")])])}],l={data:function(){return{isCollapse:!1}},methods:{goto:function(t){this.$router.push({path:t})},logout:function(){var t=this;this.$axios({method:"POST",url:"/logout",data:{userName:this.$store.state.userName,userId:this.$store.state.userId}}).then((function(s){1===s.data.status&&(t.$store.state.hasLogin=!1,t.$store.state.isManager=!1,t.$store.state.userName="",t.$store.state.email="",t.$store.state.userId="",localStorage.clear()),t.goto("/index")}))}},mounted:function(){2==this.$store.state.status&&(this.$message.error("您无管理员权限!"),this.$store.state.status=0)}},o=l,n=e(1001),r=(0,n.Z)(o,i,a,!1,null,"b5f5d330",null),c=r.exports},5080:function(t,s,e){t.exports=e.p+"assets/img/logo.2b82d36f.png"},1823:function(t,s,e){t.exports=e.p+"assets/img/logo2.ba34aa8b.png"},237:function(t,s,e){t.exports=e.p+"assets/img/logo3.33c54e88.png"}}]);
//# sourceMappingURL=615-legacy.1cea3d2f.js.map