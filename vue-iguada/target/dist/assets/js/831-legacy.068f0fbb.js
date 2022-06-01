"use strict";(self["webpackChunkwork"]=self["webpackChunkwork"]||[]).push([[831],{5831:function(t,e,a){a.r(e),a.d(e,{default:function(){return c}});var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"back"},[a("div",{staticClass:"info"},[a("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[a("el-breadcrumb-item",{attrs:{to:{path:"/"}}},[t._v("首页")]),a("el-breadcrumb-item",[t._v("校车管理")])],1)],1),a("div",{staticClass:"search"},[a("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"开始日期"},model:{value:t.start,callback:function(e){t.start=e},expression:"start"}}),a("span",[t._v("到")]),a("el-date-picker",{staticClass:"date",attrs:{type:"date",placeholder:"结束日期"},model:{value:t.end,callback:function(e){t.end=e},expression:"end"}}),a("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"medium"},on:{click:t.search}},[t._v("搜索班次")]),a("el-button",{attrs:{type:"primary",icon:"el-icon-edit",size:"medium"},on:{click:function(e){t.showAdd=!0}}},[t._v("增加班次")])],1),a("div",[a("el-table",{attrs:{data:t.times}},[a("el-table-column",{attrs:{prop:"id",label:"班次编号",width:"180"}}),a("el-table-column",{attrs:{prop:"time",label:"出发时间",width:"180"}}),a("el-table-column",{attrs:{prop:"start",label:"出发车站",width:"100"}}),a("el-table-column",{attrs:{prop:"end",label:"目的车站",width:"180"}}),a("el-table-column",{attrs:{prop:"number",label:"剩余空位",width:"100"}}),a("el-table-column",{attrs:{label:"操作",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{staticClass:"el-icon-delete-solid",attrs:{type:"text",size:"small"},on:{click:function(a){return t.del(e.row.id)}}},[t._v("删除")])]}}])})],1)],1),a("div",[a("el-pagination",{staticClass:"page",attrs:{background:"",layout:"prev, pager, next",total:t.totalPages,"current-page":t.currentPage},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"current-change":t.getPageNum}})],1),a("el-dialog",{attrs:{visible:t.showAdd,width:"30%",center:"","close-on-click-modal":!1,"lock-scroll":!1,"before-close":t.clear},on:{"update:visible":function(e){t.showAdd=e}}},[a("div",{staticClass:"add"},[a("h2",[t._v("新增校车班次")]),a("el-date-picker",{staticClass:"add-date",attrs:{type:"date",placeholder:"发车日期"},model:{value:t.newDate,callback:function(e){t.newDate=e},expression:"newDate"}}),a("span",[t._v("--")]),a("el-time-select",{staticClass:"add-date",attrs:{"picker-options":{start:"00:00",step:"1:00",end:"24:00"},placeholder:"发车时间"},model:{value:t.newTime,callback:function(e){t.newTime=e},expression:"newTime"}}),a("el-select",{staticClass:"input",attrs:{placeholder:"始发站"},model:{value:t.newStart,callback:function(e){t.newStart=e},expression:"newStart"}},[a("el-option",{attrs:{label:"长安校区",value:"长安校区"}}),a("el-option",{attrs:{label:"友谊校区",value:"友谊校区"}})],1),a("el-select",{staticClass:"input",attrs:{placeholder:"终点站"},model:{value:t.newEnd,callback:function(e){t.newEnd=e},expression:"newEnd"}},[a("el-option",{attrs:{label:"长安校区",value:"长安校区"}}),a("el-option",{attrs:{label:"友谊校区",value:"友谊校区"}})],1),a("el-button",{attrs:{type:"primary",icon:"el-icon-edit",size:"medium"},on:{click:t.add}},[t._v("增加班次")])],1)])],1)},r=[],n={data:function(){return{start:"",end:"",saveStart:"",saveEnd:"",showAdd:!1,totalPages:100,currentPage:1,newDate:"",newTime:"",newStart:"",newEnd:"",times:[]}},methods:{goto:function(t){this.$router.push({path:t})},getDate:function(t){if(""===t)return"";var e=new Date(t);return e.getFullYear()+"/"+(e.getMonth()+1)+"/"+e.getDate()},getPageNum:function(t){this.getPage(this.currentPage)},getTime:function(){return this.getDate(this.newDate)+" "+this.newTime},add:function(){var t=this;""===this.newDate||""===this.newTime||""==this.newStart||""==this.newEnd?this.$message({message:"输入不能为空!",type:"warning"}):this.newStart==this.newEnd?this.$message({message:"起点不能和终点一样!",type:"warning"}):this.$axios({method:"POST",url:"/manager/addTime",data:{page:this.currentPage,number:8,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd),time:this.getTime(),start:this.newStart,end:this.newEnd}}).then((function(e){1==e.data.status?(t.times=e.data.times,t.totalPages=10*e.data.pages,t.currentPage=e.data.currentPage,t.newDate="",t.newTime="",t.newStart="",t.newEnd="",t.showAdd=!1,t.$message({message:"添加成功",type:"success"})):0==e.data.status?t.$message({message:"日期输入错误!",type:"warning"}):2==e.data.status?(t.$store.state.status=2,t.$store.dispatch("setIsManager",!1),t.goto("/user/userInfo")):(t.$store.state.status=1,t.$store.state.hasLogin=!1,t.$store.state.isManager=!1,t.$store.state.userName="",t.$store.state.email="",t.$store.state.userId="",localStorage.clear(),t.goto("/"))}))},del:function(t){var e=this;this.$axios({method:"POST",url:"/manager/delTime",data:{id:t,page:this.currentPage,number:8,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((function(a){1==a.data.status?(e.times=a.data.times,e.totalPages=10*a.data.pages,e.currentPage=a.data.currentPage,e.$message({message:"删除成功,校车班次id为"+t,type:"success"})):0==a.data.status?e.$message.error("删除失败!"):2==a.data.status?(e.$store.state.status=2,e.$store.dispatch("setIsManager",!1),e.goto("/user/userInfo")):(e.$store.state.status=1,e.$store.state.hasLogin=!1,e.$store.state.isManager=!1,e.$store.state.userName="",e.$store.state.email="",e.$store.state.userId="",localStorage.clear(),e.goto("/"))}))},search:function(){this.saveStart=this.start,this.saveEnd=this.end,this.getPage(1)},getPage:function(t){var e=this;this.$axios({method:"GET",url:"/manager/searchTimes",params:{page:t,number:8,startTime:this.getDate(this.saveStart),endTime:this.getDate(this.saveEnd)}}).then((function(t){1==t.data.status?(e.times=t.data.times,e.totalPages=10*t.data.pages,e.currentPage=t.data.currentPage):0==t.data.status?e.$message({message:"日期输入有误!",type:"warning"}):2==t.data.status?(e.$store.state.status=2,e.$store.dispatch("setIsManager",!1),e.goto("/user/userInfo")):(e.$store.state.status=1,e.$store.state.hasLogin=!1,e.$store.state.isManager=!1,e.$store.state.userName="",e.$store.state.email="",e.$store.state.userId="",localStorage.clear(),e.goto("/"))}))},clear:function(t){this.newDate="",this.newTime="",this.newStart="",this.newEnd="",t()}},mounted:function(){this.getPage(this.currentPage)}},i=n,o=a(1001),l=(0,o.Z)(i,s,r,!1,null,"286d3444",null),c=l.exports}}]);
//# sourceMappingURL=831-legacy.068f0fbb.js.map