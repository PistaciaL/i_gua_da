<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>校车信息</el-breadcrumb-item>
                <el-breadcrumb-item>我的预约</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="search">
            <el-date-picker class="date" v-model="start" type="date" placeholder="开始日期"></el-date-picker>
            <span>到</span>
            <el-date-picker class="date" v-model="end" type="date" placeholder="结束日期"></el-date-picker>
            <el-button type="primary" icon="el-icon-search" size="medium" @click="search">搜索预约</el-button>
        </div>
        <div class="my-order" v-for="order in orders" :key="order.id">
            <div class="my-order-info clearfix">
                <div>
                    <span>班次id:{{order.id}}</span>
                    <span>发车时间:{{order.time}}</span>
                </div>
                <div>
                    <span>班次状态:{{order.status}}</span>
                    <span class="el-icon-delete-solid btn" @click="cancel(order.id)">取消预约</span>
                </div>
            </div>
            <div class="my-order-description">
                <span>{{order.start}}</span>
                <span>至</span>
                <span>{{order.end}}</span>
            </div>
        </div>
        <div>
            <el-pagination class="page" background layout="prev, pager, next" :total="totalPages" :current-page.sync="currentPage" @current-change="getPageNum">
            </el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return{
            start:'',
            end:'',
            saveStart:'',
            saveEnd:'',
            totalPages:100,
            currentPage:1,
            orders:[
            ]
        }
    },
    methods:{
        goto(path){
            this.$router.push({
                path:path
            })
        },
        getDate(value){
            if(value==='')return '';
            let time = new Date(value);
            return time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate();
        },
        getPageNum(value){
            this.getPage(this.currentPage);
        },
        search(){
            this.saveStart = this.start;
            this.saveEnd = this.end;
            this.getPage(1);
        },
        cancel(id){
            this.$axios({
                method:'POST',
                url:'/user/cancel',
                data:{
                    id:id,
                    page:this.currentPage,
                    number:4,
                    startTime:this.getDate(this.saveStart),
                    endTime:this.getDate(this.saveEnd)
                }
            }).then(res=>{
                if(res.data.status==1){
                    this.$message({
                        message:"取消成功,取消的校车班次id为"+id,
                        type:'success'
                    });
                    this.orders = res.data.orders;
                    console.log(res.data.orders);
                    this.totalPages = res.data.pages*10;
                    this.currentPage = res.data.currentPage;
                }else if(res.data.status==0){
                    this.$message({
                        message:'取消失败,该预约已过期!',
                        type:'warning'
                    })
                }else{
                    this.$store.state.status = 1;
                    this.$store.state.hasLogin = false;
                    this.$store.state.isManager = false;
                    this.$store.state.userName = '';
                    this.$store.state.email = '';
                    this.$store.state.userId = '';
                    localStorage.clear();
                    this.goto('/');
                }
            })
        },
        getPage(currentPage){
            this.$axios({
                method:'GET',
                url:'/user/searchOrders',
                params:{
                    page:currentPage,
                    number:4,
                    startTime:this.getDate(this.saveStart),
                    endTime:this.getDate(this.saveEnd)
                }
            }).then(res=>{
                if(res.data.status==1){
                    this.orders = res.data.orders;
                    this.totalPages = res.data.pages*10;
                    this.currentPage = res.data.currentPage;
                }else if(res.data.status==0){
                    this.$message({
                        message:'日期输入有误!',
                        type:'warning'
                    })
                }else{
                    this.$store.state.status = 1;
                    this.$store.state.hasLogin = false;
                    this.$store.state.isManager = false;
                    this.$store.state.userName = '';
                    this.$store.state.email = '';
                    this.$store.state.userId = '';
                    localStorage.clear();
                    this.goto('/');
                }
            })
        }
    },
    mounted(){
        this.getPage(this.currentPage);
    }
}
</script>

<style scoped>
.back{
    width: 100%;
    margin:0 auto;
}
.info{
    margin-top:10px;
    margin-bottom: 10px;
    padding-left: 10px;
}
.search{
    margin-top: 20px;
    margin-bottom: 10px;
}
.search>.date:first-child{
    margin-left: 170px;
}
.search > .el-button{
    margin-left: 40px;
}
.search > span{
    margin-left: 10px;
    margin-right: 10px;
}
.date{
    width: 150px !important;
}
/* 订单样式 */
.my-order{
    border: 1px #DCDFE6 solid;
    width: 90%;
    height: 120px;
    margin-left: 5%;
    margin-bottom: 20px;
    border-radius: 10px 10px 10px 10px;
    background-color: #FFFFFF;
}
.my-order:hover{
    box-shadow: #909399 5px 5px 10px 4px;
}
.my-order-info{
    margin-top: 5px;
}
.my-order-info>div:first-child{
    float: left;
}
.my-order-info>div:last-child{
    float: right;
}
.my-order-info>div:first-child span{
    font-weight: bold;
    color: #A9A0A0;
    margin-left: 10px;
    margin-right: 10px;
}
.btn:hover{
    color: #409EFF;
    cursor:pointer;
}
.my-order-info>div:last-child span{
    margin-right: 10px;
    margin-left: 10px;
}
.my-order-description{
    height: 90px;
}
.my-order-description>span{
    line-height: 90px;
}
.my-order-description>span:first-child{
    font-weight: bold;
    color: #363434;
    margin-left: 120px;
    margin-right: 50px;
    font-size: 40px;
}
.my-order-description>span:last-child{
    font-weight: bold;
    color: #363434;
    margin-left: 50px;
    font-size: 40px;
}
.page{
    margin-top: 20px;
    text-align: center;
}
</style>