"use strict";(self["webpackChunkwork"]=self["webpackChunkwork"]||[]).push([[475],{4475:function(t,e,a){a.r(e),a.d(e,{default:function(){return u}});var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"back"},[a("div",{staticClass:"info"},[a("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[a("el-breadcrumb-item",{attrs:{to:{path:"/"}}},[t._v("首页")]),a("el-breadcrumb-item",[t._v("校车信息")]),a("el-breadcrumb-item",[t._v("我的预约")])],1)],1),a("div",{staticClass:"search"},[a("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"开始日期"},model:{value:t.start,callback:function(e){t.start=e},expression:"start"}}),a("span",[t._v("到")]),a("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"结束日期"},model:{value:t.end,callback:function(e){t.end=e},expression:"end"}}),a("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"medium"},on:{click:t.search}},[t._v("搜索预约")])],1),t._l(t.orders,(function(e){return a("div",{key:e.id,staticClass:"my-order"},[a("div",{staticClass:"my-order-info clearfix"},[a("div",[a("span",[t._v("班次id:"+t._s(e.id))]),a("span",[t._v("发车时间:"+t._s(e.time))])]),a("div",[a("span",[t._v("班次状态:"+t._s(e.status))]),a("span",{staticClass:"el-icon-delete-solid btn",on:{click:function(a){return t.cancel(e.id)}}},[t._v("取消预约")])])]),a("div",{staticClass:"my-order-description"},[a("span",[t._v(t._s(e.start))]),a("span",[t._v("至")]),a("span",[t._v(t._s(e.end))])])])})),a("div",[a("el-pagination",{staticClass:"page",attrs:{background:"",layout:"prev, pager, next",total:t.totalPages,"current-page":t.currentPage},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"current-change":t.getPageNum}})],1)],2)},r=[],n={data:function(){return{start:"",end:"",saveStart:"",saveEnd:"",totalPages:100,currentPage:1,orders:[]}},methods:{goto:function(t){this.$router.push({path:t})},getDate:function(t){if(""===t)return"";var e=new Date(t);return e.getFullYear()+"/"+(e.getMonth()+1)+"/"+e.getDate()},getPageNum:function(t){this.getPage(this.currentPage)},search:function(){this.saveStart=this.start,this.saveEnd=this.end,this.getPage(1)},cancel:function(t){var e=this;this.$axios({method:"POST",url:"/user/cancel",data:{id:t,page:this.currentPage,number:4,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((function(a){1==a.data.status?(e.$message({message:"取消成功,取消的校车班次id为"+t,type:"success"}),e.orders=a.data.orders,console.log(a.data.orders),e.totalPages=10*a.data.pages,e.currentPage=a.data.currentPage):0==a.data.status?e.$message({message:"取消失败,该预约已过期!",type:"warning"}):(e.$store.state.status=1,e.$store.state.hasLogin=!1,e.$store.state.isManager=!1,e.$store.state.userName="",e.$store.state.email="",e.$store.state.userId="",localStorage.clear(),e.goto("/"))}))},getPage:function(t){var e=this;this.$axios({method:"GET",url:"/user/searchOrders",params:{page:t,number:4,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((function(t){1==t.data.status?(e.orders=t.data.orders,e.totalPages=10*t.data.pages,e.currentPage=t.data.currentPage):0==t.data.status?e.$message({message:"日期输入有误!",type:"warning"}):(e.$store.state.status=1,e.$store.state.hasLogin=!1,e.$store.state.isManager=!1,e.$store.state.userName="",e.$store.state.email="",e.$store.state.userId="",localStorage.clear(),e.goto("/"))}))}},mounted:function(){this.getPage(this.currentPage)}},i=n,o=a(1001),c=(0,o.Z)(i,s,r,!1,null,"0764029c",null),u=c.exports}}]);
//# sourceMappingURL=475-legacy.17a663dd.js.map