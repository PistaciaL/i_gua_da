"use strict";(self["webpackChunkwork"]=self["webpackChunkwork"]||[]).push([[584],{5584:function(t,e,s){s.r(e),s.d(e,{default:function(){return u}});var a=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"back"},[s("div",{staticClass:"info"},[s("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[s("el-breadcrumb-item",{attrs:{to:{path:"/"}}},[t._v("首页")]),s("el-breadcrumb-item",[t._v("校车信息")]),s("el-breadcrumb-item",[t._v("我的预约")])],1)],1),s("div",{staticClass:"search"},[s("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"开始日期"},model:{value:t.start,callback:function(e){t.start=e},expression:"start"}}),s("span",[t._v("到")]),s("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"结束日期"},model:{value:t.end,callback:function(e){t.end=e},expression:"end"}}),s("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"medium"},on:{click:t.search}},[t._v("搜索预约")])],1),t._l(t.orders,(function(e){return s("div",{key:e.id,staticClass:"my-order"},[s("div",{staticClass:"my-order-info clearfix"},[s("div",[s("span",[t._v("班次id:"+t._s(e.id))]),s("span",[t._v("发车时间:"+t._s(e.time))])]),s("div",[s("span",[t._v("班次状态:"+t._s(e.status))]),s("span",{staticClass:"el-icon-delete-solid btn",on:{click:function(s){return t.cancel(e.reserveId)}}},[t._v("取消预约")])])]),s("div",{staticClass:"my-order-description"},[s("span",[t._v(t._s(e.start))]),s("span",[t._v("至")]),s("span",[t._v(t._s(e.end))])])])})),s("div",{staticClass:"page"},[s("el-pagination",{attrs:{background:"",layout:"prev, pager, next",total:t.totalPages,"current-page":t.currentPage},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"current-change":t.getPageNum}})],1)],2)},r=[],i={data(){return{start:"",end:"",saveStart:"",saveEnd:"",totalPages:100,currentPage:1,orders:[]}},methods:{goto(t){this.$router.push({path:t})},getDate(t){if(""===t)return"";let e=new Date(t);return e.getFullYear()+"/"+(e.getMonth()+1)+"/"+e.getDate()},getPageNum(t){this.getPage(this.currentPage)},search(){this.saveStart=this.start,this.saveEnd=this.end,this.getPage(1)},cancel(t){this.$axios({method:"POST",url:"/user/cancel",params:{id:t,page:this.currentPage,number:4,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((e=>{let s=e.data;1==s.status?(this.$message({message:"取消成功,取消的校车班次id为"+t,type:"success"}),this.orders=s.orders,this.totalPages=10*s.pages,this.currentPage=s.currentPage):0==s.status?this.$message({message:"取消失败,该预约已过期!",type:"warning"}):(this.$store.state.status=1,this.$store.state.hasLogin=!1,this.$store.state.isManager=!1,this.$store.state.userName="",this.$store.state.email="",this.$store.state.userId="",localStorage.clear(),this.goto("/"))}))},getPage(t){this.$axios({method:"GET",url:"/user/searchOrders",params:{page:t,number:4,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((t=>{let e=t.data;1==e.status?(this.orders=e.orders,this.totalPages=10*e.pages,this.currentPage=e.currentPage):0==e.status?this.$message({message:"日期输入有误!",type:"warning"}):(this.$store.state.status=1,this.$store.state.hasLogin=!1,this.$store.state.isManager=!1,this.$store.state.userName="",this.$store.state.email="",this.$store.state.userId="",localStorage.clear(),this.goto("/"))}))}},mounted(){this.getPage(this.currentPage)}},n=i,c=s(1001),o=(0,c.Z)(n,a,r,!1,null,"bc4d48da",null),u=o.exports}}]);
//# sourceMappingURL=584.cff28ace.js.map