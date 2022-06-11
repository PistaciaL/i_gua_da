<template>
    <div class="back">
        <div class="info">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>校车信息</el-breadcrumb-item>
                <el-breadcrumb-item>校车班次</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="search">
            <el-date-picker class="date" v-model="start" type="date" placeholder="开始日期"></el-date-picker>
            <span>到</span>
            <el-date-picker class="date" v-model="end" type="date" placeholder="结束日期"></el-date-picker>
            <el-button type="primary" icon="el-icon-search" size="medium" @click="search">搜索班次</el-button>
        </div>
        <div>
            <el-table :data="times">
                <el-table-column prop="id" label="班次编号" width="180"></el-table-column>
                <el-table-column prop="time" label="出发时间" width="180"></el-table-column>
                <el-table-column prop="start" label="出发车站" width="100"></el-table-column>
                <el-table-column prop="end" label="目的车站" width="180"></el-table-column>
                <el-table-column prop="number" label="剩余空位" width="100"></el-table-column>
                <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.hasOrdered==false" @click="order(scope.row.id)" type="text" size="small">预约</el-button>
                        <el-button v-else type="text" size="small">已预约</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="page">
            <el-pagination background layout="prev, pager, next" :total="totalPages" :current-page.sync="currentPage" @current-change="getPageNum">
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
            times:[
            ],
            totalPages:100,
            currentPage:1,
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
        order(id){
            this.$axios({
                method:'POST',
                url:'/user/order',
                params:{
                    id:id,
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.$message({
                        message:"预约成功,校车班次id为"+id,
                        type:'success'
                    })
                    this.times.forEach((value,index,self)=>{
                        if(value.id==data.id){
                            value.number=data.number;
                            value.hasOrdered = data.hasOrdered;
                        }
                    })
                }else if(data.status==0){
                    this.$message({
                        message:'预约失败,已无剩余座位!',
                        type:'warning'
                    })
                }else if(data.status==2){
                    this.$message({
                        message:'预约失败,请勿重复预约!',
                        type:'warning'
                    })
                }else{
                    this.$store.state.status = 1;
                    this.$store.state.hasLogin = false;
                    this.$store.state.isManager = false;
                    this.$store.state.userName = '';
                    this.$store.state.email = '';
                    this.$store.state.userId = '';
                    this.$store.state.id = null;
                    localStorage.clear();
                    this.goto('/');
                }
            })
        },
        getPage(currentPage){
            this.$axios({
                method:'GET',
                url:'/user/searchTimes',
                params:{
                    page:currentPage,
                    number:8,
                    startTime:this.getDate(this.saveStart),
                    endTime:this.getDate(this.saveEnd)
                }
            }).then(res=>{
                let data = res.data;
                if(data.status==1){
                    this.times = data.times;
                    this.totalPages = data.pages*10;
                    this.currentPage = data.currentPage;
                }else if(data.status==0){
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
    height: 680px;
    position: relative;
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
.page{
    width: 100%;
    text-align: center;
    position: absolute;
    bottom: 0;
}
</style>